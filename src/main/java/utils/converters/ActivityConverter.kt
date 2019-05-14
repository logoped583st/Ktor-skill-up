package utils.converters

import entities.Activity
import entities.ActivityTypes
import entities.Poll
import entities.Post
import org.jetbrains.exposed.sql.transactions.transaction
import responses.ActivityResponse

fun Activity.toBaseActivity(): ActivityResponse.ActivityModel<ActivityResponse.BaseActivity> {

    return transaction {
        return@transaction when (type) {
            ActivityTypes.POLS -> ActivityResponse.ActivityModel(id.value, createdDate.toDate(), poll?.toPollModel())
            ActivityTypes.POST -> ActivityResponse.ActivityModel(id.value, createdDate.toDate(), post?.toPostModel())
            ActivityTypes.GITHUB -> TODO()
        }
    }
}

fun Poll.toPollModel(): ActivityResponse.PollModel {
    return ActivityResponse.PollModel(title, description)
}

fun Post.toPostModel(): ActivityResponse.PostModel {
    return ActivityResponse.PostModel(title, description)
}