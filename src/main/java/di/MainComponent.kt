package di

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import utils.JWTAuthorization

val kodein = Kodein {
    import(daoComponent)
    import(servicesComponent)
    bind<JWTAuthorization>() with singleton { JWTAuthorization() }
    bind<HttpClient>() with singleton { HttpClient(Apache) }
}
