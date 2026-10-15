package net.hexacell.emberweald

sealed class GameRunStatus {
    data object Proposed : GameRunStatus()
    data object Preparing : GameRunStatus()
    data object Active : GameRunStatus()
    data object Archived : GameRunStatus()

    override fun toString() = this::class.simpleName.orEmpty()
}
