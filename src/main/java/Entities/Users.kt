package Entities

import org.jetbrains.exposed.sql.Table
import java.util.*

object Users : Table() {
    val id = uuid("id").primaryKey() // task id
    val email = text("email")
    val password = text("password")
    val isAdmin = bool("isAdmin")
    val photo = text("photolink")
}

data class User(val id: UUID,
                val email: String,
                val password: String,
                val isAdmin: Boolean,
                val photo: String)