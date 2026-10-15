package net.hexacell.emberweald.server.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.hexacell.emberweald.GameRunId
import net.hexacell.emberweald.application.CreateGameRun
import net.hexacell.emberweald.application.CreateGameRunInput
import net.hexacell.emberweald.application.GetGameRun
import net.hexacell.emberweald.application.ListGameRuns
import net.hexacell.emberweald.server.dto.CreateGameRunRequest
import kotlin.uuid.Uuid

fun Application.configureGameRunRouting(
    listGameRuns: ListGameRuns,
    getGameRun: GetGameRun,
    createGameRun: CreateGameRun
) {
    routing {
        route("/api/game-runs") {
            get {
                val runs = listGameRuns()

                call.respond(runs)
            }

            post {
                val request = call.receive<CreateGameRunRequest>()
                val input = CreateGameRunInput(
                    name = request.name,
                    game = request.game,
                    participants = request.participants
                )

                val run = createGameRun(input)

                call.respond(HttpStatusCode.Created, run)
            }

            route("{id}") {
                get {
                    val idString = call.parameters["id"]
                        ?: return@get call.respond(HttpStatusCode.BadRequest)
                    val id = Uuid.parse(idString)
                    val run = getGameRun(GameRunId(id))
                        ?: return@get call.respond(HttpStatusCode.NotFound)

                    call.respond(run)
                }
            }
        }
    }
}
