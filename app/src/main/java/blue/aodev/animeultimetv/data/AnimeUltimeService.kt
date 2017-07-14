package blue.aodev.animeultimetv.data

import blue.aodev.animeultimetv.data.annotations.All
import blue.aodev.animeultimetv.data.annotations.History
import blue.aodev.animeultimetv.data.model.AnimeDetails
import blue.aodev.animeultimetv.data.model.TopAnimeId
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.model.Episode
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeUltimeService {

    @GET("series-0-1/anime/0-") @All
    fun getAllAnimes(): Single<List<AnimeSummary>>

    @GET("playlist-{id}.xml")
    fun getEpisodes(@Path("id") id: Int): Single<List<Episode>>

    @GET("index-0-1")
    fun getTopAnimes(): Single<List<TopAnimeId>>

    @GET("file-0-1/{id}")
    fun getAnimeDetails(@Path("id") id: Int): Single<AnimeDetails>

    @GET("history-0-1/{month}{year}A") @History
    fun getHistory(@Path("month") month: String, @Path("year") year: String): Single<List<AnimeSummary>>
}