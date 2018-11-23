package routes

import Entities.Task
import Service
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.routing.delete
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
        val task = call.receive<Task>()
        val service = Service(call)
        service.createTask(UUID.fromString(task.id), task.description, task.nameTask)
    }

    delete("/delete") {
        val taskId = call.parameters["id"]
        val userUUID = call.parameters["userUUID"]
        val service = Service(call)
        service.deleteTask(taskId!!.toInt(), UUID.fromString(userUUID))
    }

    post("/update") {
        val userUUID = call.parameters["userUUID"]
        val task = call.receive<Task>()
        val service = Service(call)
        service.updateTask(task.id.toInt(), UUID.fromString(userUUID), task.nameTask, task.description)
    }

}