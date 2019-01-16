package services

import db.AuthDao
import db.UserDao
import exceptions.AuthResponse

class AuthService(private val authDao: AuthDao, private val userDao: UserDao) {


    fun registration(login: String, password: String): AuthResponse {
        return if (userDao.findUserWithLogin(login)) {
            AuthResponse.TokenResponse(authDao.registration(login, password))
        } else {
            AuthResponse.UserNameAlreadyExist()
        }
    }

    fun login(login: String, password: String): AuthResponse {
        return authDao.login(login, password).let {
            if (it != null) {
                AuthResponse.TokenResponse(it)
            } else {
                AuthResponse.UserNotRegistered()
            }
        }
    }
}