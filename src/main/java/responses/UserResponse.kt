package responses

import entities.BadgeModel
import entities.SkillModel
import io.ktor.http.HttpStatusCode

sealed class UserResponse {

    data class SimpleUserModel(val id: Int,
                               val userName: String,
                               val isPrivate: Boolean,
                               val photo: String?,
                               val description: String) : UserResponse()

    data class UserModel(val id: Int,
                         val userName: String,
                         val githubLink: String?,
                         val isPrivate: Boolean,
                         val photo: String?,
                         val description: String,
                         val skills: List<SkillModel>,
                         val badges: List<BadgeModel>) : UserResponse()

    data class IncorrectBody(val message: String = "Incorrect body", val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : UserResponse()

    data class NotFound(val message: String = "user not found", val errorCode: HttpStatusCode = HttpStatusCode.NotFound) : UserResponse()

    data class Forbidden(val message: String = "user is private", val errorCode: HttpStatusCode = HttpStatusCode.Forbidden) : UserResponse()
}