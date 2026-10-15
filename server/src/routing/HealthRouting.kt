package net.hexacell.emberweald.server.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
    val modules: List<ModuleStatus>
)

@Serializable
data class ModuleStatus(
    val name: String,
    val status: String
)

fun Application.configureHealthRouting() {
    routing {
        get("/health") {
            val health = HealthResponse(
                status = "ok",
                modules = listOf(
                    ModuleStatus(name = "core", status = "ok")
                )
            )
            call.respond(health)
        }
    }
}
