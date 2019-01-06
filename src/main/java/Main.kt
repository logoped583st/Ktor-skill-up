import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import endpoints.authorization.jwtAuth
import entities.User
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.features.*
import io.ktor.http.HttpHeaders
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        appMain()
    }.start(wait = true)
}


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
}

fun Application.appMain() {
    initDb()
    jwtAuth()
    install(Compression)
    install(DefaultHeaders)
    install(CORS) {
        anyHost()
        header(HttpHeaders.AccessControlAllowOrigin)
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
            authenticate {
                route("/test"){
                    handle {
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val subjectString = principal!!.payload.subject.removePrefix("auth0|")
                        call.respondText("Success, $subjectString")
                    }
                }
            }
        }
    }
}