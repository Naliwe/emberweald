package net.hexacell.emberweald.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import net.hexacell.emberweald.GameRun

fun fetchGameRunsFromApi(): Flow<Result<List<GameRun>>> = flow {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    try {
        val response = client.get("/api/game-runs")

        if (response.status.isSuccess()) {
            val runs = response.body<List<GameRun>>()

            emit(Result.success(runs))
        } else {
            emit(Result.failure(RuntimeException("HTTP ${response.status.value}")))
        }
    } catch (e: Exception) {
        emit(Result.failure(e))
    }
}
