package utils.converters

import entities.Badge
import entities.BadgeModel
import entities.User
import entities.UserModel
import java.util.*

fun toUserModel(user: User?): UserModel =
        user?.let {
            UserModel(user.id.value,
                    user.githubLink,
                    user.photo,
                    user.description,
                    user.badges.map { toBadgeModel(it) }
            )
        }!!


fun toBadgeModel(badge: Badge): BadgeModel =
        BadgeModel(badge.badgeTitle,
                badge.badgeDescription,
                badge.badgeIcon ?: " ",
                badge.badgeDate?.toDate() ?: Date()
        )


