package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.SizedIterable


object Users : IntIdTable("user") {

    val userName = varchar("userName", 55).uniqueIndex("userName")
    val githubLink = varchar("githubLink", 255)
    val photo = varchar("photo", 255)
    val description = varchar("description", 255)

}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var userName: String by Users.userName
    var githubLink: String by Users.githubLink
    var photo: String by Users.photo
    var description by Users.description
    var badges: SizedIterable<Badge> by Badge via UsersBadges
}

data class UserModel(val id: Int,
                     val githubLink: String?,
                     val photo: String?,
                     val description: String,
                     val badges: List<BadgeModel>)







