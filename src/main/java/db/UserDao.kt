package db

import entities.User
import entities.Users
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class UserDao : BaseDAO<User> {

    override fun update(data: User): User {
        Users.update({ Users.id eq data.id }) { it ->
            it[githubLink] = data.githubLink
            it[userName] = data.userName
            it[photo] = data.photo
            it[description] = data.description
        }
        return User[data.id.value]
    }

    override fun delete(id: Int): Boolean = transaction {
        val count: Int = (Users.deleteWhere { Users.id eq id })
        return@transaction count > 0
    }

    override fun getAll(): List<User> = transaction {
        return@transaction User.all().toList()
    }

    override fun getData(id: Int): User = transaction {
        return@transaction User[id]
    }

    override fun insert(data: User): User = transaction {
        return@transaction User.new {
            this.description = data.description
            this.userName = data.userName
            this.githubLink = data.githubLink
            this.photo = data.photo
        }
    }

}