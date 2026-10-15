package net.hexacell.emberweald.ui

import net.hexacell.emberweald.api.GameRunApiDto
import kotlin.uuid.Uuid

data class GameRunUiModel(
    val id: Uuid,
    val name: String,
    val game: String,
    val status: String
)

fun GameRunApiDto.toUiModel(): GameRunUiModel =
    GameRunUiModel(
        id = id,
        name = name,
        game = game,
        status = status
    )
