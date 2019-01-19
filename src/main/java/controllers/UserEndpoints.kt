package controllers

import di.kodein
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.http.HttpMethod
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
import org.kodein.di.generic.instance
import services.UserService


val userService: UserService by kodein.instance()

@Location("/user/{id}")
data class UserWithId(val id: String)

fun Route.user() {

    authenticate{
        route("/user", HttpMethod.Get) {
            handle {
                val principal = call.authentication.principal<JWTPrincipal>()
                val subjectString = principal?.payload?.id.toString()
                call.respond(userService.getUser(subjectString))
            }
        }

        get<UserWithId> { user ->
            call.respond(userService.getUserIfIsNotPrivate(user.id))
        }
    }
}



