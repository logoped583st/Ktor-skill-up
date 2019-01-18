package db

import entities.User
import entities.Users
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import responses.UserResponse.UserModel
import utils.converters.toUserModel

class UserDao : BaseDAO<User, UserModel?> {

    override fun update(data: User): UserModel? {
        Users.update({ Users.id eq data.id }) { it ->
            it[githubLink] = data.githubLink
            it[userName] = data.userName
            it[photo] = data.photo
            it[isPrivate] = data.isPrivate
            it[description] = data.description
            it[registrationDate] = data.registrationDate
        }
        return toUserModel(User[data.id.value])
    }

    override fun delete(id: Int) = transaction {
        return@transaction User[id].delete()
    }

    override fun getAll(): List<UserModel?> = transaction {
        return@transaction User.all().map { toUserModel(it) }
    }

    override fun getData(id: Int): UserModel? = transaction {
        return@transaction toUserModel(User.findById(id))
    }

    fun getUserWithIdIfIsNotPrivate(id: Int): UserModel? = transaction {
        return@transaction toUserModel(User.find { (Users.id eq id) }.firstOrNull())
    }

    override fun insert(data: User): UserModel? = transaction {
        return@transaction toUserModel(User.new {
            this.description = data.description
            this.userName = data.userName
            this.isPrivate = false
            this.githubLink = data.githubLink
            this.photo = data.photo
            this.registrationDate = data.registrationDate
        })
    }

    fun findUserWithLogin(login: String): Boolean = transaction {
        return@transaction User.find { Users.userName eq login }.firstOrNull() == null
    }

}