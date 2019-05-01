package db

import entities.Skill
import entities.SkillModel
import entities.Skills
import entities.User
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

class SkillsDao {

    fun getData(id: Int): List<Skill> = transaction {
        return@transaction Skill.find { Skills.user eq id }.toList()
    }

    fun insertSkill(userId: Int, skill: SkillModel): SkillModel = transaction {
        Skill.new {
            this.title = skill.title
            this.description = skill.description
            this.skillLevel = skill.skillLevel
            this.user = User[userId]
        }

        return@transaction skill
    }

    fun updateSkill(userId: Int, skill: Skill): Skill = transaction {
        Skill[skill.id].run {
            this.title = skill.title
            this.description = skill.description
            this.skillLevel = skill.skillLevel
            this.user = User[userId]
        }
        return@transaction skill
    }

    fun deleteSkill(userId: Int, skillId: Int): List<Skill> {
        Skills.deleteWhere { (Skills.id eq skillId) and (Skills.user eq userId) }
        return Skill.find { (Skills.id eq userId) }.toList()
    }


}