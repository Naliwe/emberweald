package net.hexacell.emberweald

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import net.hexacell.emberweald.api.fetchGameRunsFromApi
import net.hexacell.emberweald.ui.GameRunListScreen
import net.hexacell.emberweald.ui.GameRunUiModel
import net.hexacell.emberweald.ui.toUiModel
import org.jetbrains.compose.reload.DevelopmentEntryPoint

@Composable
@Preview
@DevelopmentEntryPoint
fun App() {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Emberweald") }
                )
            }
        ) { paddingValues ->
            GameRunView(
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun GameRunView(
    modifier: Modifier = Modifier
) {
    var state by remember {
        mutableStateOf<List<GameRunUiModel>?>(null)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            fetchGameRunsFromApi().collect { result ->
                result.onSuccess { apiDtos ->
                    state = apiDtos.map { it.toUiModel() }
                }.onFailure { error ->
                    errorMessage = error.message ?: "Failed to load game runs"
                }
            }
        }
    }

    when {
        state == null && errorMessage == null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        errorMessage != null                  -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        else                                  -> {
            GameRunListScreen(
                gameRuns = state ?: emptyList(),
                modifier = modifier
            )
        }
    }
}
