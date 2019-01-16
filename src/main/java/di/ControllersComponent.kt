package di

import db.AuthDao
import io.ktor.application.ApplicationEnvironment
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import services.AuthService

val controllerComponent = Kodein.Module {
    bind<AuthService>() with factory { env: ApplicationEnvironment -> AuthService(AuthDao(env, instance()), instance()) }
}