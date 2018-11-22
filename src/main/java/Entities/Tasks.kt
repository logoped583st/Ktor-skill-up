package Entities

import Entities.Users.primaryKey
import org.jetbrains.exposed.sql.Table
import java.util.*

object Tasks : Table() {
    val uniqId = integer("uniqTaskId").primaryKey().autoIncrement()
    val id = uuid("id")
    val description = text("description")
    val nameTask = text("nameTask")
}

data class Task(val id: String,
                val nameTask:String,
                val description: String)

data class OutputTask(val uniqId:String, val id: String, val description: String,val nameTask: String)