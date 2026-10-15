package net.hexacell.emberweald.server

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import net.hexacell.emberweald.application.CreateGameRun
import net.hexacell.emberweald.application.GetGameRun
import net.hexacell.emberweald.application.ListGameRuns
import net.hexacell.emberweald.server.adapter.InMemoryGameRunRepository
import net.hexacell.emberweald.server.dto.CreateGameRunRequest
import net.hexacell.emberweald.server.dto.GameRunDto
import net.hexacell.emberweald.server.routing.HealthResponse
import net.hexacell.emberweald.server.routing.configureGameRunRouting
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val testJson = Json { ignoreUnknownKeys = true }

class ApplicationTest {

    @Test
    fun `health endpoint returns module status`() = testApplication {
        application {
            module()
        }

        val response = client.get("/health")
        assertEquals(HttpStatusCode.OK, response.status)

        val health = testJson.decodeFromString<HealthResponse>(response.bodyAsText())
        assertEquals("ok", health.status)
        assertTrue(health.modules.isNotEmpty())
    }

    @Test
    fun `list game runs returns empty list initially`() = testApplication {
        val repository = InMemoryGameRunRepository()
        application {
            install(ContentNegotiation) {
                json(testJson)
            }
            configureGameRunRouting(
                ListGameRuns(repository),
                GetGameRun(repository),
                CreateGameRun(repository)
            )
        }

        val response = client.get("/api/game-runs")
        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.bodyAsText()
        assertTrue(body.startsWith("["))
    }

    @Test
    fun `create game run and retrieve it`() = testApplication {
        val repository = InMemoryGameRunRepository()
        application {
            install(ContentNegotiation) {
                json(testJson)
            }
            configureGameRunRouting(
                ListGameRuns(repository),
                GetGameRun(repository),
                CreateGameRun(repository)
            )
        }
        val requestBody = testJson.encodeToString(
            CreateGameRunRequest(
                name = "Summer Campaign",
                game = "Minecraft"
            )
        )

        val createResponse = client.post("/api/game-runs") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        val created = createResponse.bodyAsText()
        val dto = testJson.decodeFromString<GameRunDto>(created)
        assertEquals("Summer Campaign", dto.name)
        assertEquals("Minecraft", dto.game)
        assertEquals("Proposed", dto.status)

        val id = dto.id.toString()

        val getResponse = client.get("/api/game-runs/$id")
        assertEquals(HttpStatusCode.OK, getResponse.status)

        val retrieved = getResponse.bodyAsText()
        val retrievedDto = testJson.decodeFromString<GameRunDto>(retrieved)
        assertEquals(dto.id, retrievedDto.id)
        assertEquals(dto.name, retrievedDto.name)
    }

    @Test
    fun `get non-existent game run returns 404`() = testApplication {
        val repository = InMemoryGameRunRepository()
        application {
            install(ContentNegotiation) {
                json(testJson)
            }
            configureGameRunRouting(
                ListGameRuns(repository),
                GetGameRun(repository),
                CreateGameRun(repository)
            )
        }

        val response = client.get("/api/game-runs/00000000-0000-0000-0000-000000000000")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
