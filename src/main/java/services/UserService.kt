package services

import dao.UserDao
import responses.UserResponse

class UserService(private val userDao: UserDao) {

    fun getUser(id: String): UserResponse {
        val intId: Int = try {
            id.toInt()
        } catch (ex: NumberFormatException) {
            return UserResponse.IncorrectBody()
        }

        return userDao.getData(intId) ?: UserResponse.NotFound()
    }

    fun getUserIfIsNotPrivate(id: String): UserResponse {
        val intId: Int = try {
            id.toInt()
        } catch (ex: NumberFormatException) {
            return UserResponse.IncorrectBody()
        }
        val user = userDao.getUserWithIdIfIsNotPrivate(intId)

        user?.let {
            return if (it.isPrivate) {
                UserResponse.Forbidden()
            } else {
                user
            }
        }.let {
            return UserResponse.NotFound()
        }
    }
}