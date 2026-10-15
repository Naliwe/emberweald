package net.hexacell.emberweald.application

import net.hexacell.emberweald.GameRun

class ListGameRuns(
    private val repository: GameRunRepository
) {
    suspend operator fun invoke(): List<GameRun> =
        repository.listAll()
}
