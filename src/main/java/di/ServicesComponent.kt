package di

import db.AuthDao
import io.ktor.application.ApplicationEnvironment
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import services.AuthService
import services.UserService

val servicesComponent = Kodein.Module {
    bind<AuthService>() with factory { env: ApplicationEnvironment -> AuthService(AuthDao(env, instance()), instance()) }
    bind<UserService>() with singleton { UserService(instance()) }
}