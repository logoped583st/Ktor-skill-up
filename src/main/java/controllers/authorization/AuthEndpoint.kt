package controllers.authorization

import di.kodein
import entities.locations.AuthModel
import exceptions.AuthResponse
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.application
import io.ktor.routing.post
import io.ktor.routing.route
import org.kodein.di.generic.instance
import services.AuthService


fun Route.auth() {

    val authService: AuthService by kodein.instance(arg = application.environment)

    route("/registration") {
        post {
            try {
                val model: AuthModel? = call.receive(AuthModel::class)

                model?.let {
                    val token = authService.registration(model.login, model.password)
                    call.respond(HttpStatusCode.OK, token)
                }
            } catch (e: Exception) {
                call.respond(AuthResponse.IncorrectBody())
            }
        }
    }

    route("/authorization") {
        post {
            try {
                val model: AuthModel? = call.receive(AuthModel::class)

                model?.let {
                    val token = authService.login(model.login, model.password)
                    call.respond(HttpStatusCode.OK, token)
                }
            } catch (e: Exception) {
                call.respond(AuthResponse.IncorrectBody())
            }
        }
    }
}
