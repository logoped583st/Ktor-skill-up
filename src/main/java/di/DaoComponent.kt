package di

import db.UserDao
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

val daoComponent = Kodein.Module {
    bind<UserDao>() with provider { UserDao() }
}