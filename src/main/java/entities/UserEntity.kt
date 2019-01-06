package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Users : IntIdTable() {
    val userName = varchar("userName", 55)
    val githubLink = varchar("githubLink", 255)
    val photo = varchar("photo", 255)
    val description = varchar("description", 255)
}


class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var _id = id.value
    var userName by Users.userName
    var githubLink by Users.githubLink
    var photo by Users.photo
    var description by Users.description

}




