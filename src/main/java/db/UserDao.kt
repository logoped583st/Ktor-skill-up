package db

import entities.User
import entities.UserModel
import entities.Users
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import utils.converters.toUserModel

class UserDao : BaseDAO<User, UserModel> {

    override fun update(data: User): UserModel {
        Users.update({ Users.id eq data.id }) { it ->
            it[githubLink] = data.githubLink
            it[userName] = data.userName
            it[photo] = data.photo
            it[description] = data.description
        }
        return toUserModel(User[data.id.value])
    }

    override fun delete(id: Int) = transaction {
        return@transaction User[id].delete()
    }

    override fun getAll(): List<UserModel> = transaction {
        return@transaction User.all().map { toUserModel(it) }
    }

    override fun getData(id: Int): UserModel = transaction {
        return@transaction toUserModel(User.findById(id))
    }

    override fun insert(data: User): UserModel = transaction {
        return@transaction toUserModel(User.new {
            this.description = data.description
            this.userName = data.userName
            this.githubLink = data.githubLink
            this.photo = data.photo
        })
    }

}