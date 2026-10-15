package net.hexacell.emberweald.ui

import net.hexacell.emberweald.GameRun
import net.hexacell.emberweald.GameRunStatus

data class GameRunUiModel(
    val name: String,
    val game: String,
    val status: GameRunStatus
)

fun GameRun.toUiModel(): GameRunUiModel =
    GameRunUiModel(
        name = name,
        game = game,
        status = status
    )
