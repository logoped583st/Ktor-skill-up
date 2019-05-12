package utils.converters

import entities.Activity
import entities.ActivityTypes
import entities.Poll
import entities.Post
import responses.ActivityResponse

fun Activity.toBaseActivity(): ActivityResponse.ActivityModel<ActivityResponse.BaseActivity> {
    return when (this.type) {
        ActivityTypes.POLS -> ActivityResponse.ActivityModel(this.id.value, this.createdDate.toDate(), this.poll?.toPollModel())
        ActivityTypes.POST -> ActivityResponse.ActivityModel(this.id.value, this.createdDate.toDate(), this.post?.toPostModel())
        ActivityTypes.GITHUB -> TODO()
    }
}

fun Poll.toPollModel(): ActivityResponse.PollModel {
    return ActivityResponse.PollModel(title, description)
}

fun Post.toPostModel(): ActivityResponse.PostModel {
    return ActivityResponse.PostModel(title, description)
}