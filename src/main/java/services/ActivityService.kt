package services

import controllers.ActivityInsert
import dao.ActivityDao
import io.ktor.http.HttpStatusCode
import responses.ActivityResponse
import utils.converters.toBaseActivity

class ActivityService(private val activityDao: ActivityDao) {


    fun getUserActivities(userId: String): ActivityResponse {

        val intId: Int = try {
            userId.toInt()
        } catch (ex: NumberFormatException) {
            return ActivityResponse.IncorrectUser()
        }


        return ActivityResponse.Activity(HttpStatusCode.OK, data = activityDao.getActivities(intId).map {
            it.toBaseActivity()
        })
    }

    fun insertActivity(userId: String, insertActivity: ActivityInsert) {
        val intId: Int = try {
            userId.toInt()
        } catch (ex: NumberFormatException) {
           0// ActivityResponse.IncorrectUser()
        }
        print(insertActivity.toString() +"TEST")
        activityDao.insertActivity(intId, insertActivity)
//         ActivityResponse.SingleActivity(HttpStatusCode.OK,
//                data = activityDao.insertActivity(intId, insertActivity).toBaseActivity())
    }
}
