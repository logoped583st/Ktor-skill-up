package di

import db.AuthDao
import db.UserDao
import io.ktor.application.ApplicationEnvironment
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.provider
import utils.JWTAuthorization

val daoComponent = Kodein.Module {
    bind<UserDao>() with provider { UserDao() }
    bind<AuthDao>() with factory { env: ApplicationEnvironment, jwt: JWTAuthorization -> AuthDao(env, jwt) }

}