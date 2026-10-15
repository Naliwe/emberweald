package net.hexacell.emberweald

import kotlinx.serialization.Serializable

@Serializable
sealed class GameRunStatus {
    data object Proposed : GameRunStatus()

    data object Preparing : GameRunStatus()

    data object Active : GameRunStatus()

    data object Archived : GameRunStatus()

    override fun toString() = this::class.simpleName.orEmpty()
}
