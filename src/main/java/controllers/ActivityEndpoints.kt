package controllers

import dao.ActivityDao
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.http.HttpMethod
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
import services.ActivityService


data class ActivityInsert(val title: String, val description: String, val type: String)

val activityService = ActivityService(ActivityDao())
fun Route.activities() {

    authenticate {
        route("/activities", HttpMethod.Get) {
            handle {
                val principal = call.authentication.principal<JWTPrincipal>()
                val subjectString = principal?.payload?.id.toString()
                call.respond(activityService.getUserActivities(subjectString))
            }
        }
        route("/activities", HttpMethod.Post) {
            handle {
                val principal = call.authentication.principal<JWTPrincipal>()
                val subjectString = principal?.payload?.id.toString()
                val activityInsert = call.receive<ActivityInsert>()
                call.respond(activityService.insertActivity(subjectString, activityInsert))
            }
        }

    }
}