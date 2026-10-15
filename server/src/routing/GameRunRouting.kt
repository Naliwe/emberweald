package net.hexacell.emberweald.server.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.hexacell.emberweald.GameRunId
import net.hexacell.emberweald.Participant
import net.hexacell.emberweald.application.CreateGameRun
import net.hexacell.emberweald.application.CreateGameRunInput
import net.hexacell.emberweald.application.GetGameRun
import net.hexacell.emberweald.application.ListGameRuns
import net.hexacell.emberweald.server.dto.CreateGameRunRequest
import net.hexacell.emberweald.server.dto.GameRunDto
import net.hexacell.emberweald.server.dto.ParticipantDto
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
                    .map { it.toDto() }

                call.respond(runs)
            }

            post {
                val request = call.receive<CreateGameRunRequest>()
                val input = CreateGameRunInput(
                    name = request.name,
                    game = request.game,
                    participants = request.participants.map { Participant(it.name) }
                )

                val run = createGameRun(input)
                    .toDto()

                call.respond(HttpStatusCode.Created, run)
            }

            route("{id}") {
                get {
                    val idString = call.parameters["id"]
                        ?: return@get call.respond(HttpStatusCode.BadRequest)
                    val id = Uuid.parse(idString)
                    val run = getGameRun(GameRunId(id))?.toDto()
                        ?: return@get call.respond(HttpStatusCode.NotFound)

                    call.respond(run)
                }
            }
        }
    }
}

private fun net.hexacell.emberweald.GameRun.toDto(): GameRunDto =
    GameRunDto(
        id = id.value,
        name = name,
        game = game,
        status = status.toString(),
        participants = participants.map { ParticipantDto(it.name) }
    )
