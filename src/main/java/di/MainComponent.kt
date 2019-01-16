package di

import db.AuthDao
import io.ktor.application.ApplicationEnvironment
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.singleton
import utils.JWTAuthorization

val kodein = Kodein {
    import(daoComponent)
    import(controllerComponent)
    bind<JWTAuthorization>() with singleton { JWTAuthorization() }
    bind<AuthDao>() with factory { env: ApplicationEnvironment, jwt: JWTAuthorization -> AuthDao(env, jwt) }

}
