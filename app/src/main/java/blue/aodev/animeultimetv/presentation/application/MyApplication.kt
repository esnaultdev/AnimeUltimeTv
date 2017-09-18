package blue.aodev.animeultimetv.presentation.application

import android.app.Application
import blue.aodev.animeultimetv.injection.ApplicationComponent
import blue.aodev.animeultimetv.injection.DaggerApplicationComponent
import blue.aodev.animeultimetv.injection.DomainModule
import io.realm.Realm

class MyApplication : Application() {

    companion object {
        lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        graph = DaggerApplicationComponent.builder()
                .domainModule(DomainModule())
                .build()
        graph.inject(this)

        Realm.init(this)
    }
}