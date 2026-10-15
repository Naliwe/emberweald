package net.hexacell.emberweald.application

import net.hexacell.emberweald.GameRun
import net.hexacell.emberweald.GameRunId

interface GameRunRepository {
    suspend fun listAll(): List<GameRun>
    suspend fun getById(id: GameRunId): GameRun?
    suspend fun create(run: GameRun): GameRun
}
