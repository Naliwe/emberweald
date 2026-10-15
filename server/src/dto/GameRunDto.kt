package net.hexacell.emberweald.server.dto

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class GameRunDto(
    val id: Uuid,
    val name: String,
    val game: String,
    val status: String,
    val participants: List<ParticipantDto>
)

@Serializable
data class ParticipantDto(
    val name: String
)

@Serializable
data class CreateGameRunRequest(
    val name: String,
    val game: String,
    val participants: List<ParticipantDto> = emptyList()
)
