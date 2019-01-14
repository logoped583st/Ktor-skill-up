package di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import utils.JWTAuthorization

val kodein = Kodein {
    import(daoComponent)
    bind<JWTAuthorization>() with singleton { JWTAuthorization() }
}
