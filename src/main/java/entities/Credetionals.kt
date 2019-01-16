package entities

import org.jetbrains.exposed.dao.*


object Credentials : IdTable<Int>("credential") {
    override val id = Credentials.reference("id", Users)
    val login = varchar("login", 55)
    val password = varchar("password", 255)
    val accessToken = varchar("accessToken", 500)
    val githubAccessToken = varchar("githubAccessToken", 255).nullable()
    val githubRefreshToken = varchar("githubRefreshToken", 255).nullable()
}

class Credential(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Credential>(Credentials)

    var login: String by Credentials.login
    var password: String by Credentials.password
    var accessToken: String by Credentials.accessToken
    var githubAccessToken: String? by Credentials.githubAccessToken
    var githubRefreshToken: String? by Credentials.githubRefreshToken

}

data class CredetionalModel(val accessToken: String)