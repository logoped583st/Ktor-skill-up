import com.fasterxml.jackson.databind.SerializationFeature
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import controllers.authorization.auth
import controllers.authorization.githubAuth
import controllers.user
import dao.ActivityDao
import di.kodein
import entities.*
import io.ktor.application.Application
import io.ktor.application.ApplicationStopping
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.config.HoconApplicationConfig
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.generic.instance
import utils.jwtAuth
import java.lang.reflect.Modifier
import java.text.DateFormat


class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            embeddedServer(Netty, applicationEngineEnvironment {
                config = HoconApplicationConfig(ConfigFactory.load())
                module { main() }
                connector {
                    host = config.property("ktor.debug.host").getString()
                    port = config.property("ktor.debug.port").getString().toInt()
                }
            }).start(true)
        }
    }
}


private fun initDb() {
    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = "jdbc:postgresql://localhost:5432/skill_up"
    config.username = "ws-071-11b"
    config.maximumPoolSize = 3
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    val hikariDatabase = HikariDataSource(config)
    Database.connect(hikariDatabase)

    transaction {
        exec("DO \$\$\n" +
                "BEGIN\n" +
                "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'activitytype') THEN\n" +
                "        create type activitytype AS ENUM ('POLLS', 'POST','GITHUB');\n" +
                "    END IF;\n" +
                "END\n" +
                "\$\$;")
        SchemaUtils.create(UsersBadges, Users, Badges,
                Credentials, Skills, Polls, Posts, GithubActivities, Activities,
                PollUsersAnswers, PollAnswers, Attachments, ActivityAttachemts)
    }
}

fun Application.main() {

    val httpClient: HttpClient by kodein.instance()
    httpClient.apply {
        environment.monitor.subscribe(ApplicationStopping) {
            close()
        }
    }
    print("CONFIG ${environment.log.name}")
    val issuer = environment.config.property("ktor.deployment.port").getString()
    val audience = environment.config.property("jwt.audience").getString()

    print("$issuer  $audience")
    initDb()
    jwtAuth(httpClient)
    install(Compression)
    install(Locations)
    install(DefaultHeaders)
    install(CORS) {
        anyHost()
        header(HttpHeaders.AccessControlAllowOrigin)
        header(HttpHeaders.XForwardedProto)
        header(HttpHeaders.AccessControlAllowHeaders)
        header(HttpHeaders.ContentType)
    }
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
            excludeFieldsWithModifiers(Modifier.TRANSIENT)
        }
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    val activity = ActivityDao()
    install(CallLogging)
    install(Routing) {
        routing {
            auth()
            user()
            githubAuth(httpClient)
            route("/home") {
                get {
                    call.respond(HttpStatusCode.OK,activity.getActivities())
                }
            }
        }
    }
}