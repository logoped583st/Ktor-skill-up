package utils

import com.auth0.jwk.Jwk
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import di.kodein
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import org.kodein.di.Kodein
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

fun Application.jwtAuth() {
    val jwtAuthorization: JWTAuthorization by kodein.instance()
    val issuer = environment.config.property("jwt.domain").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val realm = environment.config.property("jwt.realm").getString()

    install(Authentication) {
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