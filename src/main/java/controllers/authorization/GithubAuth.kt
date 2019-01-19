package controllers.authorization

import entities.auth.OAuthAccessTokenResponse
import io.ktor.application.call
import io.ktor.auth.OAuthServerSettings
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.http.HttpMethod
import io.ktor.locations.Location
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.param
import io.ktor.routing.route

@Location("/login/{type?}")
data class Login(val type: String?)

val loginProvider =
        OAuthServerSettings.OAuth2ServerSettings(
                name = "github",
                authorizeUrl = "https://github.com/login/oauth/authorize",
                accessTokenUrl = "https://github.com/login/oauth/access_token",
                clientId = "9442240ec9efb0c18c25",
                clientSecret = "bfd644b98395e2532b93460411a8eb8b9f945980"

        )

fun Route.githubAuth() {

    authenticate("gitHubOAuth") {

        route("/githubLogin", HttpMethod.Get) {

            param("error") {
                handle {
                    call.respondText { "error" }
                }
            }

            handle {
                val principal = call.authentication.principal<OAuthAccessTokenResponse>()
                if (principal != null) {
                    call.respondText { "Success" }
                } else {
                    call.respondText { "error" }
                }
            }
        }
    }

}