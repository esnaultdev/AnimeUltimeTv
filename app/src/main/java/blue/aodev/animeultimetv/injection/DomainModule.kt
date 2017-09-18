package blue.aodev.animeultimetv.injection

import blue.aodev.animeultimetv.data.converters.AnimeDetailsAdapter
import blue.aodev.animeultimetv.data.AnimeUltimeRepository
import blue.aodev.animeultimetv.data.AnimeUltimeService
import blue.aodev.animeultimetv.data.IRealmService
import blue.aodev.animeultimetv.data.RealmService
import blue.aodev.animeultimetv.data.converters.AnimeSummaryAdapter
import blue.aodev.animeultimetv.data.converters.EpisodeAdapter
import blue.aodev.animeultimetv.data.converters.EpisodeReleaseHistoryAdapter
import blue.aodev.animeultimetv.data.converters.TopAnimeAdapter
import blue.aodev.animeultimetv.data.local.ILocalSourceSet
import blue.aodev.animeultimetv.data.local.LocalSourceSet
import blue.aodev.animeultimetv.domain.AnimeRepository
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(HttpUrl.parse("http://www.anime-ultime.net/")!!)
                .addConverterFactory(AnimeSummaryAdapter.FACTORY)
                .addConverterFactory(EpisodeAdapter.FACTORY)
                .addConverterFactory(AnimeDetailsAdapter.FACTORY)
                .addConverterFactory(TopAnimeAdapter.FACTORY)
                .addConverterFactory(EpisodeReleaseHistoryAdapter.FACTORY)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideAnimeUltimeService(retrofit: Retrofit): AnimeUltimeService {
        return retrofit.create<AnimeUltimeService>(AnimeUltimeService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnimeRepository(animeUltimeService: AnimeUltimeService,
                               localSourceSet: ILocalSourceSet): AnimeRepository {
        return AnimeUltimeRepository(animeUltimeService, localSourceSet)
    }

    @Provides
    fun provideRealmService(): IRealmService = RealmService()

    @Provides
    fun provideLocalSourceSet(realm: IRealmService): ILocalSourceSet = LocalSourceSet(realm)
}
