package controllers.authorization

import di.kodein
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import org.kodein.di.generic.instance
import services.UserService


val userService: UserService by kodein.instance()

@Location("/user/{id}")
data class UserWithId(val id: String)

fun Route.user() {


    authenticate {
        handle {
            val principal = call.authentication.principal<JWTPrincipal>()
            val subjectString = principal?.payload?.id.toString()
            this@authenticate.get("/user") {
                call.respond(userService.getUser(subjectString))
            }

            this@authenticate.get<UserWithId> {
                call.respond(userService.getUserIfIsNotPrivate(it.id))
            }

        }
    }

}

