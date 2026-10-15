package net.hexacell.emberweald

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class GameRunId(val value: Uuid) {
    override fun toString() = value.toString()

    companion object {
        fun generate() = GameRunId(Uuid.random())
    }
}
