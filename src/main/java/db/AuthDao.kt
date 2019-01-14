package db

import com.auth0.jwt.JWT
import controllers.auth.toHui
import di.kodein
import entities.Credential
import entities.Credentials
import entities.User
import entities.UserModel
import entities.auth.JwtTokenHasher
import entities.auth.JwtTokenResponse
import entities.auth.stringlify
import io.ktor.application.ApplicationEnvironment
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.emptySized
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.generic.instance
import utils.JWTAuthorization
import utils.converters.toUserModel
import utils.encodeToken
import utils.hash

const val USER_DEFAULT_PHOTO = "https://www.teksteshqip.com/img_upz/galeri_full/157/157947.jpg"

class AuthDao(environment: ApplicationEnvironment) {

    private val jwtAuthorization: JWTAuthorization by kodein.instance()

    val issuer = environment.config.property("jwt.domain").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val realm = environment.config.property("jwt.realm").getString()

    fun registration(login: String, password: String): UserModel = transaction {
        Credential.new {
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
        }
        return@transaction toUserModel(
                User.new {
                    this.userName = login
                    this.description = ""
                    this.badges = emptySized()
                    this.photo = USER_DEFAULT_PHOTO
                    this.githubLink = ""
                })
    }

    fun login(login: String, password: String): JwtTokenResponse = transaction {
        return@transaction JwtTokenResponse((Credential.find {
            (Credentials.password eq password.hash()) and (Credentials.login eq login)
        }.firstOrNull()?.accessToken!!))
    }

}