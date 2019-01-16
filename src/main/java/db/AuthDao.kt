package db

import com.auth0.jwt.JWT
import entities.Credential
import entities.Credentials
import entities.User
import entities.auth.JwtTokenHasher
import entities.auth.JwtTokenResponse
import entities.auth.stringlify
import io.ktor.application.ApplicationEnvironment
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import utils.JWTAuthorization
import utils.encodeToken
import utils.hash

const val USER_DEFAULT_PHOTO = "https://www.teksteshqip.com/img_upz/galeri_full/157/157947.jpg"

class AuthDao(private val environment: ApplicationEnvironment, val jwtAuthorization: JWTAuthorization) {

    //private val jwtAuthorization: JWTAuthorization by kodein.instance()

    val issuer = environment.config.property("jwt.domain").getString()
    val audience = environment.config.property("jwt.audience").getString()

    fun registration(login: String, password: String): String = transaction {

        val user = User.new {
            this.userName = login
            this.description = ""
            this.photo = USER_DEFAULT_PHOTO
            this.githubLink = ""
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
                    .withClaim("token", encodeToken(JwtTokenHasher(login, password, id.value.toString()).stringlify()))
                    .sign(jwtAuthorization.algorithm)
        }.accessToken



        return@transaction token
    }

    fun login(login: String, password: String): String? = transaction {
        return@transaction Credential.find {
            (Credentials.password eq password.hash()) and (Credentials.login eq login)
        }.firstOrNull()?.accessToken
    }

}