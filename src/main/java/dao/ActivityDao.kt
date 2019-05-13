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
            this.type = ActivityTypes.POST
           // this.type = ActivityTypes.valueOf(insertedActivity.type)
        }

//        when (ActivityTypes.valueOf(insertedActivity.type)) {
//            ActivityTypes.POLS -> Poll.new {
//                this.title = insertedActivity.title
//                //this.activityId = activity
//                this.description = insertedActivity.description
//            }
//            ActivityTypes.POST -> Post.new {
//                this.title = insertedActivity.title
//                this.description = insertedActivity.description
//                //this.activityId
//            }
//            ActivityTypes.GITHUB -> TODO()
//        }

       // return@transaction activity
    }


}



