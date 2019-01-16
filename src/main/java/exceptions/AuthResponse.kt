package exceptions

import io.ktor.http.HttpStatusCode

sealed class AuthResponse {

    object Nothing : AuthResponse()

    data class TokenResponse(val token: String) : AuthResponse()

    data class UserNameAlreadyExist(val message: String = "User with this nickname already exist",
                                    val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse()

    data class IncorrectBody(val message: String = "Incorrect body", val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : AuthResponse()

    data class UserNotRegistered(val message: String = "Incorrect login or password",
                                 val errorCode: HttpStatusCode = HttpStatusCode.Conflict) : AuthResponse()
}