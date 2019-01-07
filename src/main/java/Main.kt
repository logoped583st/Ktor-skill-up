import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import db.UserDao
import com.auth0.jwt.*
import endpoints.authorization.JWTAuthorization
import endpoints.authorization.jwtAuth
import entities.Badges
import entities.Users
import entities.UsersBadges
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.features.*
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
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
        SchemaUtils.create(Users, UsersBadges, Badges)
    }
}

fun Application.main() {
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
                route("/test") {
                    handle {
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val subjectString = principal?.payload?.id.toString()

                        call.respondText("Success, $subjectString")
                    }
                }
            }
            get("/get") {


                call.respond(HttpStatusCode.OK, (UserDao().getData(1)))
            }
            get("/token") {
                val issuer = environment.config.property("jwt.domain").getString()
                val audience = environment.config.property("jwt.audience").getString()

                val a = JWT.create()
                        .withIssuer(issuer)
                        .withAudience(audience)
                        .withJWTId("test")
                        .sign(JWTAuthorization.algorithm)

                call.respondText { a }
            }
        }
    }
}