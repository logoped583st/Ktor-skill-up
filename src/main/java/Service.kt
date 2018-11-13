import Entities.Task
import Entities.Tasks
import Entities.User
import Entities.Users
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.collections.ArrayList

class Service(val call: ApplicationCall) {

    private fun toUser(row: ResultRow): User =
            User(
                    id = row[Users.id],
                    email = row[Users.email],
                    password = row[Users.password],
                    photo = row[Users.photo],
                    isAdmin = row[Users.isAdmin]
            )


    private fun toTask(row: ResultRow): Task =
            Task(row[Tasks.id], row[Tasks.description])

    suspend fun auth(login: String, password: String) {

        var userOutput: User? = null
        transaction {
            val user = toUser(Users.select {
                (Users.email eq login)
            }.first())
            if (user.password == Hasher.hash(password)) {
                userOutput = user
            }
        }

        if (userOutput == null) {
            call.respond(HttpStatusCode(404, "User Not Found"), "User Not Found")
        } else {
            call.respond(HttpStatusCode(200, "okey"), userOutput!!)
        }
    }

    suspend fun reg(login: String, password: String) {
        var response: User? = null
        transaction {
            Users.select()
            {
                Users.email eq login
            }.firstOrNull().also {
                if (it == null) {
                    Users.insert { us ->
                        us[email] = login
                        us[Users.password] = Hasher.hash(password)
                        us[photo] = "https://www.teksteshqip.com/img_upz/allart_full/63351.jpg"
                        us[id] = UUID.randomUUID()
                        us[isAdmin] = false
                    }
                    response = toUser(Users.select { (Users.email eq login) }.first())
                } else {
                    response = null
                }
            }
        }


        if (response == null) {
            call.respond(HttpStatusCode(401, "Username already exist"), "Username already exist")

        } else {
            call.respond(HttpStatusCode(200, "Okey"), response!!)
        }
    }


    suspend fun getAllUsers(email: String) {
        var response: List<User>? = null
        transaction {
            Users.select {
                (Users.email eq email)
            }.first().let {
                print(toUser(it))
                if (toUser(it).isAdmin) {
                    response = Users.selectAll().map { us -> toUser(us) }
                }
            }

        }

        if (response == null) {
            call.respond(HttpStatusCode(401, "Username already exist"), "Username already exist")

        } else {
            call.respond(HttpStatusCode(200, "Okey"), response!!)
        }
    }

    suspend fun getAllTasks(id: UUID) {
        val tasks: ArrayList<Task> = ArrayList()
        transaction {
            Tasks.select {
                Tasks.id eq id
            }.map {
                tasks.add(toTask(it))
            }
        }
        if (tasks.isEmpty()) {
            call.respond(HttpStatusCode(401, "Username already exist"), "Username already exist")

        } else {
            call.respond(HttpStatusCode(200, "Okey"), tasks)
        }
    }

    suspend fun createTask(id: UUID, description: String): Task {
        transaction {
            Tasks.insert { it ->
                it[Tasks.id] = id
                it[Tasks.description] = description
            }
        }
        call.respond(HttpStatusCode(200, "Okey"), return Task(id, description)
        )

    }


    suspend fun getTask(id: UUID): Task? = transaction {
        Tasks.select {
            (Tasks.id eq id)
        }.mapNotNull { toTask(it) }
                .singleOrNull()
    }
}
