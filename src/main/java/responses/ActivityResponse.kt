package responses

import entities.Poll
import io.ktor.http.HttpStatusCode
import java.util.*

sealed class ActivityResponse(open val codeResult: HttpStatusCode, open val data: Any) {


    open class BaseActivity

    data class Activity<T : BaseActivity>(override val codeResult: HttpStatusCode = HttpStatusCode.OK, override val data: List<ActivityModel<T>>) : ActivityResponse(codeResult, data)

    data class ActivityModel<T : BaseActivity>(val id: Int, val createdDate: Date, val description: T?)

    data class PostModel(val title: String, val description: String) : BaseActivity()

    data class PollModel(val title: String, val description: String) : BaseActivity()

    data class IncorrectUser(override val data: ErrorMessage = ErrorMessage("Incorrect user"),
                             val errorCode: HttpStatusCode = HttpStatusCode.BadRequest) : ActivityResponse(errorCode, data)
}