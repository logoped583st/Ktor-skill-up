package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable


object Users : IntIdTable() {
    val userName = varchar("userName", 55)
    val githubLink = varchar("githubLink", 255).nullable()
    val photo = varchar("photo", 255).nullable()
    val description = varchar("description", 255)
}

class User(id: EntityID<Int>) : IntEntity(id), Serializable {
    companion object : IntEntityClass<User>(Users)

    var userName: String by Users.userName
    var githubLink: String? by Users.githubLink
    var photo: String? by Users.photo
    var description by Users.description
    var badges by Badge via UsersBadges


}

data class UserModel(val id: Int,
                     val githubLink: String?,
                     val photo: String?,
                     val description: String,
                     val badges: List<BadgeModel>)







