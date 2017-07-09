package blue.aodev.animeultimetv.injection

import blue.aodev.animeultimetv.presentation.animedetails.AnimeDetailsFragment
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.episodes.EpisodesFragment
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
    fun inject(mainFragment: AnimeDetailsFragment)
    fun inject(mainFragment: EpisodesFragment)
}