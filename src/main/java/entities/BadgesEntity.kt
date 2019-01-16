package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table
import java.util.*

object Badges : IntIdTable() {

    val badgeTitle = varchar("badgeTitle", 55)
    val badgeDescription = varchar("badgeDescription", 255)
    val badgeIcon = varchar("badgeIcon", 255).nullable()
    val badgeDate = date("badgeData").nullable()
}


class Badge(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Badge>(Badges)

    var badgeTitle by Badges.badgeTitle
    var badgeDescription by Badges.badgeDescription
    var badgeIcon by Badges.badgeIcon
    var badgeDate by Badges.badgeDate

}

object UsersBadges : Table() {
    val badgeId = reference("badgeId", Badges).primaryKey(0).nullable()
    val userId = reference("userId", Users).primaryKey(1).nullable()
}


data class BadgeModel(val badgeTitle: String,
                      val badgeDescription: String,
                      val badgeIcon: String,
                      val badgeData: Date)
