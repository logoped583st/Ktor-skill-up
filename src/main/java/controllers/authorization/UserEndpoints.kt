package controllers.authorization

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.user() {
    authenticate {
        handle {
            val principal = call.authentication.principal<JWTPrincipal>()
            val subjectString = principal?.payload?.id.toString()
            this@user.route("") {


            }
        }


    }

}