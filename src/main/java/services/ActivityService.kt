package services

import dao.ActivityDao
import dao.UserDao
import io.ktor.http.HttpStatusCode
import responses.ActivityResponse
import utils.converters.toBaseActivity

class ActivityService(private val activityDao: ActivityDao, private val userDao: UserDao) {


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
}
