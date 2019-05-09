package dao

import entities.*
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

class BadgesDao {

    fun getData(): List<Badge> = transaction {
        return@transaction Badge.all().toList()
    }

    fun insertBadge(userId: Int, badge: BadgeModel): Badge = transaction {

        return@transaction Badge.new {
            badgeDate = DateTime(badge.badgeData)
            badgeDescription = badge.badgeDescription
            badgeIcon = badge.badgeIcon
            badgeTitle = badge.badgeTitle
        }
    }

    fun updateBadge(userId: Int, skill: SkillModel): SkillModel = transaction {
        Skills.update({ Skills.user eq userId }) {
            it[title] = skill.title
            it[description] = skill.description
            it[skillLevel] = skill.skillLevel
        }

        return@transaction skill
    }

    fun getUserBadges(userId: Int): List<Badge> = transaction {
        return@transaction User[userId].badges.toList()
    }

    fun deleteBadge(badgeId: Int): Int {
        return Badges.deleteWhere { Badges.id eq badgeId }
    }
}