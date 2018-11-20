import Entities.Tasks
import Entities.Users
import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
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

    fun initDb() {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:tcp://localhost/~/test"
        config.username = "lab7"
        config.maximumPoolSize = 3
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        val hikariDatabase = HikariDataSource(config)
        Database.connect(hikariDatabase)
        transaction {
            create(Users,Tasks)
            Users.insert { us ->
                us[email] = "admin"
                us[Users.password] = Hasher.hash("admin")
                us[photo] = "https://www.teksteshqip.com/img_upz/allart_full/63351.jpg"
                us[id] = UUID.randomUUID()
                us[isAdmin] = true
            }
        }
    }

    fun Application.main() {
        initDb()
        install(Compression)
        install(DefaultHeaders)
        install(CORS) {
            anyHost()
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


    }

}