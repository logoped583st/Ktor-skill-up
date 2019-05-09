package dao

import entities.Skill
import entities.Skills
import entities.User
import entities.Users
import org.jetbrains.exposed.sql.transactions.transaction
import responses.UserResponse
import responses.UserResponse.UserModel
import utils.converters.toSimpleUserMode
import utils.converters.toUserModel

class UserDao {


    fun getAll(): List<UserResponse.SimpleUserModel?> = transaction {
        return@transaction User.all().map { toSimpleUserMode(it) }
    }

    fun getData(id: Int): UserModel? = transaction {
        val skills = Skill.find { Skills.user eq id }.toList()
        return@transaction toUserModel(User[id], skills)
    }

    fun getUserWithIdIfIsNotPrivate(id: Int): UserModel? = transaction {
        val skills = Skill.find { Skills.user eq id }.toList()
        return@transaction toUserModel(User.find { (Users.id eq id) }.firstOrNull(), skills)
    }


    fun findUserWithLogin(login: String): Boolean = transaction {
        return@transaction User.find { Users.userName eq login }.firstOrNull() == null
    }

}