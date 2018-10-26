package Entities

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object Users : Table() {
    val id = uuid("id").primaryKey()
    val email = text("email")
    val password = text("password")
    //val skills: Column<String> = text("skill")
    val photo = text("photolink")
}

data class User(val id: UUID,
                val email: String,
                val password: String,
                val photo: String)