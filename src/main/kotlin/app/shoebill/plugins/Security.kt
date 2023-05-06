package app.shoebill.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.sessions.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureSecurity() {
    @Serializable
    data class MySession(val displayName: String)
    install(Sessions) {
        cookie<MySession>("USER_SESSION") {
            cookie.extensions["SameSite"] = "lax"
            cookie.maxAgeInSeconds = 3600
        }
    }
    install(CORS) {
        allowCredentials = true
        allowHost("localhost:5173")
        allowOrigins { when (it) {
            "localhost:5173" -> true
            else -> false
        }}
    }
    install(ContentNegotiation) {
        json()
    }
    routing {
        options("/session") {
            call.respond(HttpStatusCode.OK)
        }
        post("/session") {
            val formParameters = call.receiveParameters()
            val displayName = formParameters["displayName"].toString()
            call.sessions.set(MySession(displayName = displayName))
            call.respondRedirect("http://localhost:5173")
        }
        get("/session") {
            val userSession = call.sessions.get<MySession>()
            when (userSession) {
                null -> call.respond(HttpStatusCode(405, "Not Permitted"))
                else -> call.respond(userSession)
            }
        }
    }
}
