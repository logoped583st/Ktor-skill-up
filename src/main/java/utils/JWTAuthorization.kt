package utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import controllers.authorization.Login
import controllers.authorization.loginProvider
import di.kodein
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.auth.oauth
import io.ktor.client.HttpClient
import io.ktor.locations.url
import org.kodein.di.generic.instance
import javax.crypto.KeyGenerator


class JWTAuthorization {

    private val secret = KeyGenerator.getInstance("AES").generateKey()
    val algorithm = Algorithm.HMAC256("secret")


    //We will sign our JWT with our ApiKey secret
    fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier = JWT
            .require(algorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
}

fun Application.jwtAuth(oauthHttpClient: HttpClient) {
    val issuer = "https://jwt-provider-domain/"
    val audience = "jwt-audience"
    val realm = "ktor sample app"
    val jwtAuthorization: JWTAuthorization by kodein.instance()
  //  val issuer = environment.config.property("jwt.domain").getString()
  //  val audience = environment.config.property("jwt.audience").getString()
   // val realm = environment.config.property("jwt.realm").getString()

    install(Authentication) {
        oauth("gitHubOAuth") {
            client = oauthHttpClient
            providerLookup = { loginProvider }
            urlProvider = { url(Login(it.name)) }
        }
        val jwtVerifier =jwtAuthorization.makeJwtVerifier(issuer, audience)
        jwt {
            verifier(jwtVerifier)
            this.realm = realm
            validate { credential ->
                if (credential.payload.audience.contains(audience))
                    JWTPrincipal(credential.payload)
                else
                    null
            }
        }
    }
}