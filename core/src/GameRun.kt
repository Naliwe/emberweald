package net.hexacell.emberweald

data class GameRun(
    val id: GameRunId,
    val name: String,
    val game: String,
    val status: GameRunStatus,
    val participants: List<Participant>
) {
    init {
        require(name.isNotBlank()) { "Run name must not be blank" }
        require(game.isNotBlank()) { "Game title must not be blank" }
    }
}
