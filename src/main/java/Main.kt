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
import routes.auth
import java.util.*


class Main {

    fun initDb() {
        val config = HikariConfig()
//        config.jdbcUrl = "jdbc:postgresql://localhost:5433/postgres"
//        config.dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
//        config.username = "postgres"
//        config.password = "root"
//        config.maximumPoolSize = 5
//        config.validate()
//        val hikariDatabase = HikariDataSource(config)
//        Database.connect(hikariDatabase)
//

        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:tcp://localhost/~/test"
        config.username = "sa"
        config.maximumPoolSize = 3
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        val hikariDatabase = HikariDataSource(config)
        Database.connect(hikariDatabase)
        transaction {
            create(Users)
            Users.insert {
                it[email] = "logoped"
                it[password] = Hasher.hash("logoped")
                it[id] = UUID.randomUUID()
                it[photo] = "https://avatars3.githubusercontent.com/u/30079690?s=400&u=e3443b749fa4d36979e0d631192f3a7bbadc3eeb&v=4"
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
            }
        }


    }

}