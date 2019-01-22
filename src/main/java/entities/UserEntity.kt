package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.SizedIterable


object Users : IntIdTable() {

    val userName = varchar("userName", 55).uniqueIndex("userName")
    val githubLink = varchar("githubLink", 255)
    val photo = varchar("photo", 255)
    val description = varchar("description", 255)
    val isPrivate = bool("isPrivate")
    val registrationDate = date("registrationDate")
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var userName: String by Users.userName
    var githubLink: String by Users.githubLink
    var isPrivate: Boolean by Users.isPrivate
    var photo: String by Users.photo
    var description by Users.description
    var badges: SizedIterable<Badge> by Badge via UsersBadges
    var registrationDate by Users.registrationDate
}








