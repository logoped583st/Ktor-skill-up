package routes

import Service
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.auth() {


    get("/registration") {
        val service: Service = Service(call)
        val login = call.parameters["login"]
        val password = call.parameters["password"]
        print(" SUKA $login$password")
        service.reg(login!!, password!!)
    }

    get("/authorization") {
        val login = call.parameters["login"]
        val password = call.parameters["password"]
        val service: Service = Service(call)
        print("route")
        service.auth(login!!, password!!)
    }


}