package routes

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import Service
import java.util.*

fun Route.admin() {
    get("/allusers") {
        val login = call.parameters["login"]
        val service = Service(call = call)
        service.getAllUsers(login!!)

    }
}
