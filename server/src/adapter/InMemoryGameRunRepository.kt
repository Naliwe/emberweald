package net.hexacell.emberweald.server.adapter

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.hexacell.emberweald.GameRun
import net.hexacell.emberweald.GameRunId
import net.hexacell.emberweald.application.GameRunRepository

class InMemoryGameRunRepository : GameRunRepository {
    private val runs = mutableMapOf<GameRunId, GameRun>()
    private val mutex = Mutex()

    override suspend fun listAll(): List<GameRun> =
        mutex.withLock { runs.values.toList() }

    override suspend fun getById(id: GameRunId): GameRun? =
        mutex.withLock { runs[id] }

    override suspend fun create(run: GameRun): GameRun =
        mutex.withLock {
            runs[run.id] = run
            run
        }
}
