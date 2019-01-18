package controllers.authorization

import di.kodein
import entities.locations.AuthModel
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.application
import io.ktor.routing.post
import io.ktor.routing.route
import org.kodein.di.generic.instance
import responses.AuthResponse
import services.AuthService


fun Route.auth() {

    val authService: AuthService by kodein.instance(arg = application.environment)

    route("/registration") {
        post {
            try {
                val model: AuthModel? = call.receive(AuthModel::class)

                model?.let {
                    val authResponse = authService.registration(model.login, model.password)
                    call.respond(authResponse.codeResult, authResponse.data)
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
                    val authResponse = authService.login(model.login, model.password)
                    call.respond(authResponse.codeResult, authResponse.data)
                }
            } catch (e: Exception) {
                call.respond(AuthResponse.IncorrectBody())
            }
        }
    }
}
