package blue.aodev.animeultimetv.domain

import blue.aodev.animeultimetv.domain.model.Anime
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.model.EpisodeReleaseSummary
import blue.aodev.animeultimetv.domain.model.Timing
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface AnimeRepository {

    fun isInitialized(): Observable<Boolean>

    fun getAnimes(ids: List<Int>): Observable<List<AnimeSummary>>

    fun search(query: String): Observable<List<AnimeSummary>>

    fun getAnimeSummary(id: Int): Observable<AnimeSummary>

    fun getAnime(id: Int): Observable<Anime>

    fun getTopAnimes(): Observable<List<AnimeSummary>>

    fun getRecentEpisodes(): Observable<List<EpisodeReleaseSummary>>

    fun getTimings(): Single<List<Timing>>

    fun saveTiming(timing: Timing): Completable
}