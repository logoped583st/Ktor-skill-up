package Entities

import Entities.Users.primaryKey
import org.jetbrains.exposed.sql.Table
import java.util.*

object Tasks : Table() {
    val id = uuid("id").primaryKey()
    val description = text("description")
}

data class Task(val id: UUID,
                val description: String)