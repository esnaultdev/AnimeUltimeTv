package blue.aodev.animeultimetv.domain

import blue.aodev.animeultimetv.data.EpisodeInfo
import io.reactivex.Observable
import io.reactivex.Single

interface AnimeRepository {

    fun getAnimes(): Observable<List<AnimeInfo>>

    fun search(query: String): Observable<List<AnimeInfo>>

    fun getAnime(id: Int): Observable<AnimeInfo>

    // TODO don't expose this, but a way to request Animes
    fun getEpisodesInfo(id: Int): Single<List<EpisodeInfo>>
}