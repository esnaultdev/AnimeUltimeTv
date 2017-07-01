package blue.aodev.animeultimetv.domain

import io.reactivex.Observable

interface AnimeRepository {

    fun getAnimes(): Observable<List<AnimeSummary>>

    fun search(query: String): Observable<List<AnimeSummary>>

    fun getAnimeSummary(id: Int): Observable<AnimeSummary>

    fun getAnime(id: Int): Observable<Anime>
}