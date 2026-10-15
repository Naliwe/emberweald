package net.hexacell.emberweald

data class Participant(
    val name: String
) {
    init {
        require(name.isNotBlank()) { "Participant name must not be blank" }
    }
}
