package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Skills : IntIdTable() {

    val title = varchar("title", 55)
    val description = varchar("description", 255)
    val skillLevel = enumeration("Level", SkillLevel::class)
    val user = reference("userId", Users)
}

enum class SkillLevel(level: String) {
    BEGINNER("Beginner"),

    ELEMENTARY("Elementary"),

    PRE_INTERMEDIATE("Pre Intermediate"),

    INTERMEDIATE("Intermediate"),

    UPPER_INTERMEDIATE("Upper Intermediate"),

    ADVANCED("Advanced"),

    Proficient("Proficient")
}

class Skill(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Skill>(Skills)

    var title by Skills.title
    var description by Skills.description
    var skillLevel: SkillLevel by Skills.skillLevel
    var user by User referencedOn Skills.user
}

data class SkillModel(val id: Int, val title: String, val description: String, val skillLevel: SkillLevel)
