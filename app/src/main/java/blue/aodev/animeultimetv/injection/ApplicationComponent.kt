package blue.aodev.animeultimetv.injection

import android.support.v17.leanback.app.DetailsFragment
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.main.MainFragment
import blue.aodev.animeultimetv.presentation.search.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DomainModule::class))
interface ApplicationComponent {
    fun inject(application: MyApplication)

    fun inject(mainFragment: MainFragment)
    fun inject(mainFragment: SearchFragment)
    fun inject(mainFragment: DetailsFragment)
}