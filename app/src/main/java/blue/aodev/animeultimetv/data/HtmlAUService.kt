package blue.aodev.animeultimetv.data

import io.reactivex.Single
import retrofit2.http.GET

interface HtmlAUService {

    @GET("series-0-1/anime/0-")
    fun getAllAnimes(): Single<List<AnimeInfo>>
}