import Entities.*
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.jetbrains.exposed.sql.*
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
            Task(row[Tasks.id].toString(),
                    row[Tasks.description],
                    row[Tasks.nameTask])

    private fun toOutPutTask(row: ResultRow): OutputTask = OutputTask(
            row[Tasks.uniqId].toString(),
            row[Tasks.id].toString(),
            row[Tasks.description],
            row[Tasks.nameTask])

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
        val tasks: ArrayList<OutputTask> = ArrayList()
        transaction {
            Tasks.select {
                Tasks.id eq id
            }.map {
                tasks.add(toOutPutTask(it))
            }
        }

        call.respond(HttpStatusCode(200, "Okey"), tasks)

    }

    suspend fun createTask(id: UUID, description: String, nameTask: String) {
        var uuid: Int? = null
        transaction {
            uuid = Tasks.insert { it ->
                it[Tasks.id] = id
                it[Tasks.description] = description
                it[Tasks.nameTask] = nameTask
            } get Tasks.uniqId
        }
        call.respond(HttpStatusCode(200, "Okey"), OutputTask(uuid.toString(), id.toString(), description, nameTask))
    }


    suspend fun getTask(id: UUID): Task? = transaction {
        Tasks.select {
            (Tasks.id eq id)
        }.mapNotNull { toTask(it) }
                .singleOrNull()
    }

    suspend fun getUser(id: UUID) {
        val user = transaction {
            return@transaction toUser(Users.select {
                (Users.id eq id)
            }.single())
        }
        call.respond(HttpStatusCode(200, "Okey"), user)
    }

    suspend fun deleteTask(id: Int, userUUID: UUID) {
        val task: List<OutputTask> = transaction {
            (Tasks.deleteWhere {
                (Tasks.uniqId eq id) and (Tasks.id eq userUUID)
            })
            return@transaction Tasks.select {
                Tasks.id eq userUUID
            }.map {
                toOutPutTask(it)
            }
        }
        call.respond(HttpStatusCode(200, "Okey"), task)
    }

    suspend fun updateTask(id: Int, userUUID: UUID, taskName: String, taskDescription: String) {
        val task: List<OutputTask> = transaction {
            Tasks.update({ (Tasks.uniqId eq id) and (Tasks.id eq userUUID) }) {
                it[description] = taskDescription
                it[nameTask] = taskName
            }

            return@transaction Tasks.select {
                Tasks.id eq userUUID
            }.map {
                toOutPutTask(it)
            }
        }
        call.respond(HttpStatusCode(200, "Okey"), task)
    }

}
