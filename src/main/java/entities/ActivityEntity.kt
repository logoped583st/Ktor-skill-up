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
    override val id: Column<EntityID<Int>> = reference("id", Activities)

    val title = varchar("pollTitle", 55)
    val description = varchar("pollDescription",255)

}

object PollAnswers : IdTable<Int>() {
    override val id: Column<EntityID<Int>> = reference("id", Polls)
    val answer = varchar("pollAnswer", 55)

}

class PollAnswer(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollAnswer>(PollAnswers)
    var usersVoted: SizedIterable<User> by User via PollUsersAnswers
}

class Poll(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Poll>(Polls)


}

object PollUsersAnswers : Table() {
    val pollAnswerId = reference("pollAnswerId", PollAnswers).primaryKey(0).nullable()
    val userId = reference("userAnsweredId", Users).primaryKey(1).nullable()
}

object Posts : IdTable<Int>() {
    override val id: Column<EntityID<Int>> = reference("id", Activities)
    val title = varchar("postTitle", 55)

}

class Post(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Post>(Posts)

}


object GithubActivities : IdTable<Int>() {
    override val id: Column<EntityID<Int>> = reference("id", Activities)
    val title = varchar("githubTitle", 55)

}

class GithubAcitvity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GithubAcitvity>(GithubActivities)

}


