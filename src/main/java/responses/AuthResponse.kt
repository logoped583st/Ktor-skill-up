package responses

import io.ktor.http.HttpStatusCode

sealed class AuthResponse(open val codeResult: HttpStatusCode, open val data: Any) {

    data class TokenResponse(override val data: Token, override val codeResult: HttpStatusCode = HttpStatusCode.OK) : AuthResponse(codeResult, data)

    data class UserNameAlreadyExist(override val data: String ="User with this nickname already exist",
                                    override val codeResult: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse(codeResult, data)

    data class IncorrectBody(val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse(errorCode, "test")

    data class UserNotRegistered(override val data: String = "Incorrect login or password",
                                 val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse(errorCode, data)

    data class BadLogin(override val data: String = "GithubLogin to small",
                        val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse(errorCode, data)

    data class BadPassword(override val data: ErrorMessage = ErrorMessage(
            "Password must have eight characters, at least one uppercase letter, one lowercase letter and one number"),
                           val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse(errorCode, data)

    data class Token(val token: String)

}