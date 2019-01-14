package endpoints.authorization

import entities.locations.AuthModel
import exceptions.AuthExceptions
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route


fun Route.auth() {
    route("/registrations") {
        post {
            try {
                val model: AuthModel? = call.receive(AuthModel::class)
                call.respondText { "TEST" }
            }catch (e:Exception){
                call.respond(AuthExceptions.IncorrectBody("bad body"))

            }
        }
    }
}
