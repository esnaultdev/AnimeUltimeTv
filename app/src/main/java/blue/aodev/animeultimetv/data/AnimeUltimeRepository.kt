package blue.aodev.animeultimetv.data

import blue.aodev.animeultimetv.domain.Anime
import blue.aodev.animeultimetv.domain.AnimeSummary
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.domain.Episode
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class AnimeUltimeRepository(val animeUltimeService: AnimeUltimeService) : AnimeRepository {

    private val behaviorSubject = BehaviorSubject.createDefault<List<AnimeSummary>>(emptyList())

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

    override fun getAnimes(): Observable<List<AnimeSummary>> {
        return behaviorSubject
    }

    override fun search(query: String): Observable<List<AnimeSummary>> {
        return behaviorSubject.map { it.filter { it.title.startsWith(query, true) } }
    }

    override fun getAnimeSummary(id: Int): Observable<AnimeSummary> {
        return behaviorSubject.map { it.find { it.id == id } }
    }

    override fun getAnime(id: Int): Observable<Anime> {
        return Observable.combineLatest(getAnimeSummary(id),
                animeUltimeService.getEpisodes(id).toObservable(),
                animeZipper)
    }

    private val animeZipper = BiFunction<AnimeSummary, List<Episode>, Anime>{
        summary, episodes -> summary.toAnime(episodes)
    }
}