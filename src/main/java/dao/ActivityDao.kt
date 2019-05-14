package dao

import controllers.ActivityInsert
import entities.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime


class ActivityDao {

    fun getActivities(id: Int): List<Activity> = transaction {
        return@transaction Activity.find { Activities.userId eq id }.toList()
    }

    fun insertActivity(id: Int, insertedActivity: ActivityInsert) = transaction {
        val activity = Activity.new {
            this.createdDate = DateTime.now()
            this.userId = User[id]
            this.type = getType(insertedActivity.type)
            this.type = ActivityTypes.valueOf(insertedActivity.type)
        }

        when (ActivityTypes.valueOf(insertedActivity.type)) {
            ActivityTypes.POLS -> Poll.new {
                this.title = insertedActivity.title
                this.activityId = activity
                this.description = insertedActivity.description
            }
            ActivityTypes.POST -> Post.new {
                this.title = insertedActivity.title
                this.description = insertedActivity.description
                this.activityId = activity
            }
            ActivityTypes.GITHUB -> TODO()
        }

        return@transaction activity
    }

    fun removiActivity(userId: Int, activityId: Int): List<Activity> = transaction {

        Activity[activityId].post?.delete()
        Activity[activityId].delete()
        return@transaction Activity.find { Activities.userId eq userId }.toList()
    }

    fun updateActivity(userId: Int, activityId: Int, insertedActivity: ActivityInsert) = transaction {

        when (ActivityTypes.valueOf(insertedActivity.type)) {
            ActivityTypes.POLS -> Poll[activityId].apply {
                this.title = insertedActivity.title
                this.description = insertedActivity.description
            }
            ActivityTypes.POST -> Post[activityId].apply {
                this.title = insertedActivity.title
                this.description = insertedActivity.description
            }
            ActivityTypes.GITHUB -> TODO()
        }

        return@transaction Activity.find { Activities.userId eq userId }.toList()

    }

}



