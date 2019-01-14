package exceptions

import io.ktor.http.HttpStatusCode

sealed class AuthExceptions {
    data class IncorrectBody(val message: String= "Incorrect body",val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : AuthExceptions()
}