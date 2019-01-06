package entities.auth

import io.ktor.auth.*
import io.ktor.http.Parameters

sealed class OAuthAccessTokenResponse : Principal {
    data class OAuth1(
            val token: String,
            val tokenSecret: String,
            val extraParameters: Parameters = Parameters.Empty
    ) : OAuthAccessTokenResponse()

    data class OAuth2(
            val accessToken: String,
            val tokenType: String,
            val expiresIn: Long,
            val refreshToken: String?,
            val extraParameters: Parameters = Parameters.Empty
    ) : OAuthAccessTokenResponse()
}
