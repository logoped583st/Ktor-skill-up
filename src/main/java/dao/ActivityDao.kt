package dao

import entities.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime


class ActivityDao {

    fun getActivities(id: Int): List<Activity> = transaction {
        return@transaction Activity.find { Activities.userId eq id }.toList()
    }

    fun insertActivity(id: Int, activityType: ActivityTypes) {
        val activity = Activity.new {
            this.createdDate = DateTime.now()
            this.userId = User[id]
            this.type = activityType

        }
        when (activityType) {
            ActivityTypes.POLS -> Poll.new {
                this.title = ""
                this.activityId = activityId
                this.description = ""
            }
            ActivityTypes.POST -> Post.new {
                this.title = ""
                this.description
                this.activityId = activityId
            }
            ActivityTypes.GITHUB -> TODO()
        }

    }


}



