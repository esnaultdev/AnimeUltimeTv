package blue.aodev.animeultimetv.domain

import io.reactivex.Observable

interface AnimeUltimeRepository {

    fun getAnimes(): Observable<List<AnimeInfo>>

    fun search(query: String): Observable<List<AnimeInfo>>

    fun getAnime(id: Int): Observable<AnimeInfo>
}