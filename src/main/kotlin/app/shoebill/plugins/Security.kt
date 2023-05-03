package app.shoebill.plugins

import io.ktor.http.*
import io.ktor.server.sessions.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*

fun Application.configureSecurity() {
    data class MySession(val displayName: String)
    install(Sessions) {
        cookie<MySession>("USER_SESSION") {
            cookie.extensions["SameSite"] = "lax"
            cookie.maxAgeInSeconds = 3600
        }
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.ContentType)
        allowHeader("USER_SESSION")
        exposeHeader("USER_SESSION")
        allowHost("localhost:3000")
        allowCredentials = true
        maxAgeInSeconds = 3600
    }
    routing {
        options("/session") {
            call.respond(HttpStatusCode.OK)
        }
        get("/session") {
            val userSession = call.sessions.get<MySession>()
            when (userSession) {
                null -> call.respondText("Nope")
                else -> call.respondText("Yup")
            }
        }
        post("/session") {
            call.sessions.set(MySession(displayName = "derp"))
            call.respondRedirect("http://localhost:3000/session")
        }
    }
}
