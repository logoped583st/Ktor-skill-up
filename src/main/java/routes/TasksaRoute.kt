package routes

import Service
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import java.util.*

fun Route.tasks() {

    get("/tasks") {
        val id = call.parameters["id"]
        val service = Service(call)
        service.getAllTasks(UUID.fromString(id))
    }

    post(path = "/createTask") {
        val id = call.parameters["id"]
        val description = call.parameters["description"]
        val service = Service(call)
        service.createTask(UUID.fromString(id), description!!)
    }


}