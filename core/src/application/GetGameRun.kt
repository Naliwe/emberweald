package net.hexacell.emberweald.application

import net.hexacell.emberweald.GameRun
import net.hexacell.emberweald.GameRunId

class GetGameRun(
    private val repository: GameRunRepository
) {
    suspend operator fun invoke(id: GameRunId): GameRun? =
        repository.getById(id)
}
