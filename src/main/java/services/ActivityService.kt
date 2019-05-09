package services

import dao.ActivityDao
import dao.UserDao
import responses.ActivityResponse

class ActivityService(private val activityDao: ActivityDao, private val userDao: UserDao) {


    fun getUserActivities(userId: String): ActivityResponse {

        val intId: Int = try {
            userId.toInt()
        } catch (ex: NumberFormatException) {
            return ActivityResponse.IncorrectUser()
        }

       // return  ActivityResponse.Activity(activityDao.getActivities(intId))
    }

}

fun login(login: String, password: String): ActivityResponse {

}
}