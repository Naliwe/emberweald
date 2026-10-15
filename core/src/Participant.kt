package net.hexacell.emberweald

import kotlinx.serialization.Serializable

@Serializable
data class Participant(
    val name: String
) {
    init {
        require(name.isNotBlank()) { "Participant name must not be blank" }
    }
}
