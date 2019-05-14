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
                    skill.map { it.toSkillModel() },
                    user.badges.map { it.toBadgeModel() }
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


fun Badge.toBadgeModel(): BadgeModel =
        BadgeModel(
                id.value,
                badgeTitle,
                badgeDescription,
                badgeIcon ?: " ",
                badgeDate?.toDate() ?: Date()
        )

fun Skill.toSkillModel(): SkillModel =
        SkillModel(
                id.value,
                title,
                description,
                skillLevel)


