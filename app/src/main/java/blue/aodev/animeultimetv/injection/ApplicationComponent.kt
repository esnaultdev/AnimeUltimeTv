package blue.aodev.animeultimetv.injection

import blue.aodev.animeultimetv.presentation.screen.animedetails.AnimeDetailsFragment
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.screen.episodes.EpisodesFragment
import blue.aodev.animeultimetv.presentation.screen.launch.LaunchActivity
import blue.aodev.animeultimetv.presentation.screen.main.MainFragment
import blue.aodev.animeultimetv.presentation.screen.search.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DomainModule::class))
interface ApplicationComponent {
    fun inject(application: MyApplication)

    fun inject(mainFragment: LaunchActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(mainFragment: SearchFragment)
    fun inject(mainFragment: AnimeDetailsFragment)
    fun inject(mainFragment: EpisodesFragment)
}