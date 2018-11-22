package routes

import Service
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import java.util.*

fun Route.auth() {

    get("/registration") {
        val service = Service(call)
        val login = call.parameters["login"]
        val password = call.parameters["password"]
        service.reg(login!!, password!!)
    }

    get("/authorization") {
        val login = call.parameters["login"]
        val password = call.parameters["password"]
        val service = Service(call)
        print("route")
        service.auth(login!!, password!!)
    }

    get("/getUser") {
        val id = call.parameters["id"]
        val service = Service(call)
        service.getUser(UUID.fromString(id))
    }


}