package net.hexacell.emberweald

import kotlin.uuid.Uuid

data class GameRunId(val value: Uuid) {
    override fun toString() = value.toString()

    companion object {
        fun generate() = GameRunId(Uuid.random())
    }
}
