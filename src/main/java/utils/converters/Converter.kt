package utils.converters

import entities.*
import responses.UserResponse
import java.util.*

fun toUserModel(user: User?, skill: List<Skill>): UserResponse.UserModel? =
        user?.let {
            UserResponse.UserModel(user.id.value,
                    user.userName,
                    user.githubLink,
                    user.isPrivate,
                    user.photo,
                    user.description,
                    skill.map { toSkillModel(it) },
                    user.badges.map { toBadgeModel(it) }
            )
        }

fun toSimpleUserMode(user: User?): UserResponse.SimpleUserModel? =
        user?.let {
            UserResponse.SimpleUserModel(
                    user.id.value,
                    user.userName,
                    user.isPrivate,
                    user.photo,
                    user.description
            )
        }


fun toBadgeModel(badge: Badge): BadgeModel =
        BadgeModel(badge.badgeTitle,
                badge.badgeDescription,
                badge.badgeIcon ?: " ",
                badge.badgeDate?.toDate() ?: Date()
        )

fun toSkillModel(skill: Skill): SkillModel =
        SkillModel(skill.title,
                skill.description,
                skill.skillLevel)


