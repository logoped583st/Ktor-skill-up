package db

import com.auth0.jwt.JWT
import entities.Credential
import entities.Credentials
import entities.User
import io.ktor.application.ApplicationEnvironment
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import responses.AuthResponse
import utils.JWTAuthorization
import utils.hash

const val USER_DEFAULT_PHOTO = "https://www.teksteshqip.com/img_upz/galeri_full/157/157947.jpg"

class AuthDao(environment: ApplicationEnvironment, private val jwtAuthorization: JWTAuthorization) {

    private val issuer = environment.config.property("jwt.domain").getString()
    private val audience = environment.config.property("jwt.audience").getString()

    fun registration(login: String, password: String): AuthResponse.Token = transaction {

        val user = User.new {
            this.userName = login
            this.description = ""
            this.photo = USER_DEFAULT_PHOTO
            this.githubLink = ""
            this.isPrivate = false
            this.registrationDate = DateTime.now()
        }

        user.badges = SizedCollection(emptyList())

        val token = Credential.new(user.id.value) {
            this.login = login
            this.githubAccessToken = null
            this.githubRefreshToken = null
            this.password = password.hash()
            this.accessToken = JWT.create()
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .withJWTId(id.value.toString())
                    .sign(jwtAuthorization.algorithm)
        }.accessToken

        return@transaction AuthResponse.Token(token)
    }

    fun login(login: String, password: String): AuthResponse.Token? = transaction {
        return@transaction Credential.find {
            (Credentials.password eq password.hash()) and (Credentials.login eq login)
        }.firstOrNull()?.accessToken?.let { AuthResponse.Token(it) }
    }

}