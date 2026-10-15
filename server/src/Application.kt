package net.hexacell.emberweald.server

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json
import net.hexacell.emberweald.application.CreateGameRun
import net.hexacell.emberweald.application.GetGameRun
import net.hexacell.emberweald.application.ListGameRuns
import net.hexacell.emberweald.server.adapter.InMemoryGameRunRepository
import net.hexacell.emberweald.server.routing.configureGameRunRouting
import net.hexacell.emberweald.server.routing.configureHealthRouting

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val json = Json {
        ignoreUnknownKeys = true
    }

    install(ContentNegotiation) {
        json(json)
    }

    install(CORS) {
        allowHost("*", schemes = listOf("http", "https"))
        allowMethod(io.ktor.http.HttpMethod.Get)
        allowMethod(io.ktor.http.HttpMethod.Post)
        allowMethod(io.ktor.http.HttpMethod.Options)
        allowHeader(io.ktor.http.HttpHeaders.ContentType)
    }

    val repository = InMemoryGameRunRepository()
    val listGameRuns = ListGameRuns(repository)
    val getGameRun = GetGameRun(repository)
    val createGameRun = CreateGameRun(repository)

    configureHealthRouting()
    configureGameRunRouting(listGameRuns, getGameRun, createGameRun)
}
