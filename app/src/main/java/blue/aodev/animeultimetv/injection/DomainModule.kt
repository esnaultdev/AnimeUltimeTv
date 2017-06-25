package blue.aodev.animeultimetv.injection

import blue.aodev.animeultimetv.data.AnimeInfoAdapter
import blue.aodev.animeultimetv.data.AnimeUltimeRepository
import blue.aodev.animeultimetv.data.AnimeUltimeService
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
                .addConverterFactory(AnimeInfoAdapter.FACTORY)
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
    fun provideAnimeRepository(animeUltimeService: AnimeUltimeService): AnimeRepository {
        return AnimeUltimeRepository(animeUltimeService)
    }

}
