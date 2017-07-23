package blue.aodev.animeultimetv.domain

import blue.aodev.animeultimetv.domain.model.Anime
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.model.EpisodeReleaseSummary
import io.reactivex.Observable

interface AnimeRepository {

    fun isInitialized(): Observable<Boolean>

    fun getAnimes(ids: List<Int>): Observable<List<AnimeSummary>>

    fun search(query: String): Observable<List<AnimeSummary>>

    fun getAnimeSummary(id: Int): Observable<AnimeSummary>

    fun getAnime(id: Int): Observable<Anime>

    fun getTopAnimes(): Observable<List<AnimeSummary>>

    fun getRecentEpisodes(): Observable<List<EpisodeReleaseSummary>>
}