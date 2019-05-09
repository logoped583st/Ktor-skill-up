package dao

import entities.Activities
import entities.Activity
import entities.Badge
import org.jetbrains.exposed.sql.transactions.transaction


class ActivityDao {

    fun getActivities(id:Int): List<Activity> = transaction {
        return@transaction Activity.find { Activities.userId eq id }.toList()
    }


}



