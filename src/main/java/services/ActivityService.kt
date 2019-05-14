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

    fun insertActivity(userId: String, insertActivity: ActivityInsert): ActivityResponse {
        val intId: Int = try {
            userId.toInt()
        } catch (ex: NumberFormatException) {
            0// ActivityResponse.IncorrectUser()
        }
        print(insertActivity.toString() + "TEST")
//        activityDao.insertActivity(intId, insertActivity)
        return ActivityResponse.SingleActivity(HttpStatusCode.OK,
                data = activityDao.insertActivity(intId, insertActivity).toBaseActivity())
    }

    fun deleteActivity(userId: String, activityId: Int): ActivityResponse.Activity<ActivityResponse.BaseActivity> {
        val intId: Int = try {
            userId.toInt()
        } catch (ex: NumberFormatException) {
            0// ActivityResponse.IncorrectUser()
        }

        return ActivityResponse.Activity(HttpStatusCode.OK,
                data = activityDao.removiActivity(intId, activityId).map { it.toBaseActivity() })
    }


    fun updateActivity(userId: String, activityId: Int, insertActivity: ActivityInsert): ActivityResponse.Activity<ActivityResponse.BaseActivity> {
        val intId: Int = try {
            userId.toInt()
        } catch (ex: NumberFormatException) {
            0// ActivityResponse.IncorrectUser()
        }

        return ActivityResponse.Activity(HttpStatusCode.OK,
                data = activityDao.updateActivity(intId, activityId, insertActivity).map { it.toBaseActivity() })
    }
}
