package responses

import io.ktor.http.HttpStatusCode

sealed class AuthResponse(open val codeResult: HttpStatusCode, open val data: Any) {

    data class TokenResponse(override val data: Token, override val codeResult: HttpStatusCode = HttpStatusCode.OK) : AuthResponse(codeResult, data)

    data class UserNameAlreadyExist(override val data: ErrorMessage = ErrorMessage("User with this nickname already exist"),
                                    override val codeResult: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse(codeResult, data)

    data class IncorrectBody(override val data: ErrorMessage = ErrorMessage("Incorrect body"),
                             val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse(errorCode, data)

    data class UserNotRegistered(override val data: ErrorMessage = ErrorMessage("Incorrect Login or password"),
                                 val errorCode: HttpStatusCode = HttpStatusCode.Conflict) : AuthResponse(errorCode, data)

    data class BadLogin(override val data: ErrorMessage = ErrorMessage("Login to small"),
                        val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse(errorCode, data)

    data class BadPassword(override val data: ErrorMessage = ErrorMessage(
            "Password must have eight characters, at least one uppercase letter, one lowercase letter and one number"),
                           val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse(errorCode, data)

    data class Token(val token: String)

}