package services

import com.sendgrid.*
import controllers.ActivityInsert
import dao.ActivityDao
import dao.KET
import entities.User
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.transactions.transaction
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

    suspend fun insertActivity(userId: String, insertActivity: ActivityInsert): ActivityResponse {
        val intId: Int = try {
            userId.toInt()
        } catch (ex: NumberFormatException) {
            0// ActivityResponse.IncorrectUser()
        }

        coroutineScope {
            transaction {
                val from = Email("test@gmail.com")
                val subject = "Skill-up"
                val to = Email("logoped583st@mail.ru")
                val content = Content("text/plain", "You add new activitity!!! GRAC!")
                val mail = Mail(from, subject, to, content)
                println(mail.toString())

                val sg = SendGrid(KET)
                val request = Request()
                try {
                    request.method = Method.POST
                    request.endpoint = "mail/send"
                    request.body = mail.build()
                    val response = sg.api(request)
                    System.out.println(response.statusCode)
                    System.out.println(response.body)
                    System.out.println(response.headers)
                } catch (ex: Exception) {
                    throw ex
                }
            }

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
