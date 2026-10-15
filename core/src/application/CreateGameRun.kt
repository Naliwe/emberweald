package net.hexacell.emberweald.application

import net.hexacell.emberweald.GameRun
import net.hexacell.emberweald.GameRunId
import net.hexacell.emberweald.GameRunStatus
import net.hexacell.emberweald.Participant

data class CreateGameRunInput(
    val name: String,
    val game: String,
    val participants: List<Participant> = emptyList()
)

class CreateGameRun(
    private val repository: GameRunRepository
) {
    suspend operator fun invoke(input: CreateGameRunInput): GameRun {
        val run = GameRun(
            id = GameRunId.generate(),
            name = input.name,
            game = input.game,
            status = GameRunStatus.Proposed,
            participants = input.participants
        )
        return repository.create(run)
    }
}
