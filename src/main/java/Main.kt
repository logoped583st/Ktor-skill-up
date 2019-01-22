import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import controllers.authorization.auth
import controllers.authorization.githubAuth
import controllers.user
import di.kodein
import entities.*
import io.ktor.application.Application
import io.ktor.application.ApplicationStopping
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.generic.instance
import utils.jwtAuth
import java.text.DateFormat


class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            embeddedServer(Netty, port = 8080) {
                main()
            }.start(wait = true)
        }
    }
}


private fun initDb() {
    val config = HikariConfig()
    config.driverClassName = "org.h2.Driver"
    config.jdbcUrl = "jdbc:h2:tcp://localhost/~/db/logopedServer"
    config.username = "logoped"
    config.maximumPoolSize = 3
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    val hikariDatabase = HikariDataSource(config)
    Database.connect(hikariDatabase)

    transaction {
        SchemaUtils.create(UsersBadges, Users, Badges,
                Credentials, Skills, Polls, Posts, GithubActivities, Activities,
                PollAnswers, PollUsersAnswers)
    }
}

fun Application.main() {

    val httpClient: HttpClient by kodein.instance()
    httpClient.apply {
        environment.monitor.subscribe(ApplicationStopping) {
            close()
        }
    }

    initDb()
    jwtAuth(httpClient)
    install(Compression)
    install(Locations)
    install(DefaultHeaders)
    install(CORS) {
        anyHost()
        header(HttpHeaders.AccessControlAllowOrigin)
        header(HttpHeaders.XForwardedProto)
    }
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(CallLogging)
    install(Routing) {
        routing {
            auth()
            user()
            githubAuth(httpClient)
            route("/home") {
                get {
                    call.respondText { "Success" }
                }
            }
        }
    }
}