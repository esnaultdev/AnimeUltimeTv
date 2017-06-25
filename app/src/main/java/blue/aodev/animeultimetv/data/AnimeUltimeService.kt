package blue.aodev.animeultimetv.data

import blue.aodev.animeultimetv.domain.AnimeInfo
import io.reactivex.Single
import retrofit2.http.GET

interface AnimeUltimeService {

    @GET("series-0-1/anime/0-")
    fun getAllAnimes(): Single<List<AnimeInfo>>
}