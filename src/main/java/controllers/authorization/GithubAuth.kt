package controllers.authorization

import com.google.gson.annotations.SerializedName
import entities.auth.OAuthAccessTokenResponse
import io.ktor.application.call
import io.ktor.auth.OAuthServerSettings
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.location
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.param
import kotlinx.coroutines.async

@Location("/login/{type?}")
data class Login(val type: String?)


@Location("/login/github")
data class GithubCode(val code: String, val state: String)

data class GithubToken(
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("token_type")
        val tokenType: String,
        @SerializedName("scope")
        val scope: List<String>)

val loginProvider =
        OAuthServerSettings.OAuth2ServerSettings(
                name = "github",
                authorizeUrl = "https://github.com/login/oauth/authorize",
                accessTokenUrl = "https://github.com/login/oauth/access_token", //TODO move client_id and client_secret to config file
                clientId = "9442240ec9efb0c18c25",
                clientSecret = "bfd644b98395e2532b93460411a8eb8b9f945980"

        )

fun Route.githubAuth(httpClient: HttpClient) {

    get<GithubCode> {

        val httpRequestBuilder = HttpRequestBuilder()
        httpRequestBuilder.url("https://github.com/login/oauth/access_token?client_id=9442240ec9efb0c18c25" +
                "&client_secret=bfd644b98395e2532b93460411a8eb8b9f945980&code=${it.code}")
        httpRequestBuilder.header("Accept", "application/json")
        val req1 = async { httpClient.call(httpRequestBuilder).response.receive<GithubToken>() }
        val res = req1.await()
        call.respondText { res.toString() }
    }

    authenticate("gitHubOAuth") {
        location<Login> {
            param("error") {
                handle {
                    call.respondText { call.parameters.getAll("error").toString() }
                }
            }

            handle {
                val principal = call.authentication.principal<OAuthAccessTokenResponse>()
                if (principal != null) {
                    call.respondText(principal.toString())
                }
            }
        }
    }


}