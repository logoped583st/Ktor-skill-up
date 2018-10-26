package routes

import Entities.AuthCreds
import Service
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post

fun Route.auth() {


    post("/registration") {
        val creds = call.receive<AuthCreds>()
        val service: Service = Service(call)

        service.reg(creds.email!!, creds.password!!)
    }

    get("/authorization") {
        val login = call.parameters["login"]
        val password = call.parameters["password"]
        val service: Service = Service(call)
        print("route")
        service.auth(login!!, password!!)
    }


}