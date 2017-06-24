package blue.aodev.animeultimetv.data

import retrofit2.Call
import retrofit2.http.GET

interface HtmlAUService {

    @GET("series-0-1/anime/0-")
    fun getAllAnimes(): Call<List<AnimeInfo>>
}