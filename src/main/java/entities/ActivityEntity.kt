package entities

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.Table

object Activities : IntIdTable() {
    val table = varchar("tableName", 255)
    val userId = reference("userId", Users.id)
    val createdDate = date("createdDate")
}


class Activity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Activity>(Activities)

    var tableName by Activities.table
}

object Polls : IdTable<Int>() {
    override val id: Column<EntityID<Int>> = reference("id", Activities).primaryKey()
    val title = varchar("pollTitle", 55)
    val description = varchar("pollDescription",255)

}

object PollAnswers : IntIdTable() {
    val title = varchar("answer",55)
    val poll = reference("pollId",Polls)
}


class PollAnswer(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollAnswer>(PollAnswers)

}


object PollUsersAnswers : IntIdTable() {
    val userId = reference("userAnsweredId", Users)
    val pollAnswerId = reference("pollAnswerId", PollAnswers)
}

object Posts : IdTable<Int>() {
    override val id: Column<EntityID<Int>> = reference("id", Activities).primaryKey()
    val title = varchar("postTitle", 55)

}

class Post(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Post>(Posts)

}


object GithubActivities : IdTable<Int>() {
    override val id: Column<EntityID<Int>> = reference("id", Activities).primaryKey()
    val title = varchar("githubTitle", 55)

}

class GithubAcitvity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GithubAcitvity>(GithubActivities)

}


