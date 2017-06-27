package blue.aodev.animeultimetv.data

import blue.aodev.animeultimetv.domain.AnimeInfo
import blue.aodev.animeultimetv.domain.AnimeRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class AnimeUltimeRepository(val animeUltimeService: AnimeUltimeService) : AnimeRepository {

    private val behaviorSubject = BehaviorSubject.createDefault<List<AnimeInfo>>(emptyList())

    init {
        animeUltimeService.getAllAnimes()
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .subscribeBy(
                        onNext = { behaviorSubject.onNext(it) },
                        onError = { behaviorSubject.onError(it) }
                        // Ignore the onComplete as we do not want the subject to complete
                )
    }

    override fun getAnimes(): Observable<List<AnimeInfo>> {
        return behaviorSubject
    }

    override fun search(query: String): Observable<List<AnimeInfo>> {
        return behaviorSubject.map { it.filter { it.title.startsWith(query, true) } }
    }

    override fun getAnime(id: Int): Observable<AnimeInfo> {
        return behaviorSubject.map { it.find { it.id == id } }
    }

    override fun getEpisodesInfo(id: Int): Single<List<EpisodeInfo>> {
        return animeUltimeService.getEpisodesInfo(id)
    }
}