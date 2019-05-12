package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.postgresql.util.PGobject

object Activities : IntIdTable() {
    val userId = reference("userId", Users.id)
    val createdDate = date("createdDate")
    val type = customEnumeration("type", "activityType", { value -> ActivityTypes.valueOf(value as String) }, { PGEnum("activityType", it) })
}


class Activity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Activity>(Activities)

    var userId by User referencedOn Activities.userId
    var createdDate by Activities.createdDate


    var type by Activities.type
    val poll by Poll optionalBackReferencedOn (Polls.activityId)
    val post by Post optionalBackReferencedOn (Posts.activityId)
}

object Polls : IntIdTable() {
    val title = varchar("pollTitle", 55)
    val description = varchar("pollDescription", 255)
    val activityId = reference("activityId", Activities.id)

}

class Poll(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Poll>(Polls)

    var title by Polls.title
    var description by Polls.description
    var activityId by Polls.activityId

}

object PollAnswers : IntIdTable() {
    val title = varchar("answer", 55)
    val poll = reference("pollId", Polls.id)

}


class PollAnswer(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollAnswer>(PollAnswers)

}


object PollUsersAnswers : IntIdTable() {
    val userId = reference("userAnsweredId", Users)
    val pollAnswerId = reference("pollAnswerId", PollAnswers)
}

object Posts : IntIdTable() {
    val title = varchar("postTitle", 55)
    val description = varchar("postDescription", 55)
    val activityId = reference("activityId", Activities.id)
}

class Post(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Post>(Posts)

    var title by Posts.title
    var description by Posts.description
    var activityId by Posts.activityId
}


object GithubActivities : IntIdTable() {
    val title = varchar("githubTitle", 55)
    val activityId = reference("activityId", Activities.id)
}

class GithubAcitvity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GithubAcitvity>(GithubActivities)

}


class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}


enum class ActivityTypes(type: String) {
    POLS("POLLS"),
    POST("POST"),
    GITHUB("GITHUB")
}

fun getTable(type: ActivityTypes): String {
    return when (type) {

        ActivityTypes.POST -> "ASD"
        ActivityTypes.GITHUB -> "DAS"
        else -> "DASD"
    }
}




