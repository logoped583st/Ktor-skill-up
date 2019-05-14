package services

import dao.AuthDao
import dao.UserDao
import responses.AuthResponse

class AuthService(private val authDao: AuthDao, private val userDao: UserDao) {


    fun registration(login: String, password: String): AuthResponse {

        if (login.length < 2) {
            return AuthResponse.BadLogin()
        }

        val passwordRegex = Regex("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*\$")

        if (!password.matches(passwordRegex)) {
            return AuthResponse.BadPassword()
        }

        return if (userDao.findUserWithLogin(login)) {
            AuthResponse.TokenResponse(authDao.registration(login, password))
        } else {
            AuthResponse.UserNameAlreadyExist()
        }
    }

    fun login(login: String, password: String): AuthResponse {

        return authDao.login(login, password).let {
            if (it != null) {
                AuthResponse.TokenResponse(AuthResponse.Token(it.token))
            } else {
                AuthResponse.UserNotRegistered()
            }
        }
    }
}