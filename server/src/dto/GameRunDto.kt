package net.hexacell.emberweald.server.dto

import kotlinx.serialization.Serializable
import net.hexacell.emberweald.Participant

@Serializable
data class CreateGameRunRequest(
    val name: String,
    val game: String,
    val participants: List<Participant> = emptyList()
)
