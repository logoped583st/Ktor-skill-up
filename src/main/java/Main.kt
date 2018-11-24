import Entities.Tasks
import Entities.Tasks.description
import Entities.Users
import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.routing.routing
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import routes.admin
import routes.auth
import routes.tasks
import java.util.*


class Main {

    private fun initDb() {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:tcp://localhost/~/test"
        config.username = "sa"
        config.maximumPoolSize = 3
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        val hikariDatabase = HikariDataSource(config)
        Database.connect(hikariDatabase)
        parseXmlTasks()

        transaction {
            if (!Users.exists()) {

            }
//            if (!Tasks.exists()) {
//                SchemaUtils.create(Tasks)
//                parseXmlTasks().toList().forEach { it ->
//                    Tasks.insert { task ->
//                        task[description] = it.description
//                        task[id] = UUID.fromString(it.id)
//                        task[nameTask] = it.nameTask
//                        task[uniqId] = it.uniqId.toInt()
//                    }
//                }
//
//            }
           create(Users, Tasks)
//            Users.insert { us ->
//                us[email] = "admin"
//                us[Users.password] = Hasher.hash("admin")
//                us[photo] = "https://www.teksteshqip.com/img_upz/allart_full/63351.jpg"
//                us[id] = UUID.randomUUID()
//                us[isAdmin] = true
//            }
        }

    }

    fun Application.main() {
        initDb()
        install(Compression)
        install(DefaultHeaders)
        install(CORS) {
            anyHost()

            (HttpMethod.DefaultMethods).forEach { it ->
                this.method(it)
            }
            header(HttpHeaders.AccessControlAllowOrigin)
            allowCredentials = true
            header(HttpHeaders.XForwardedProto)
        }
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }
        install(CallLogging)
        install(Routing) {
            routing {
                auth()
                admin()
                tasks()
            }
        }

       // writeXmlFile()
    }
}