package utils.converters

import entities.Badge
import entities.BadgeModel
import entities.User
import responses.UserResponse
import java.util.*

fun toUserModel(user: User?): UserResponse.UserModel? =
        user?.let {
            UserResponse.UserModel(user.id.value,
                    user.userName,
                    user.githubLink,
                    user.isPrivate,
                    user.photo,
                    user.description,
                    user.badges.map { toBadgeModel(it) }
            )
        }


fun toBadgeModel(badge: Badge): BadgeModel =
        BadgeModel(badge.badgeTitle,
                badge.badgeDescription,
                badge.badgeIcon ?: " ",
                badge.badgeDate?.toDate() ?: Date()
        )


