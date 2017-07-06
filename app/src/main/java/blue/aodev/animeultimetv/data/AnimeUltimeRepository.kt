package blue.aodev.animeultimetv.data

import blue.aodev.animeultimetv.domain.model.Anime
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.domain.model.Episode
import io.reactivex.Observable
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
        return Observable.combineLatest(
                listOf(
                        getAnimeSummary(id),
                        animeUltimeService.getEpisodes(id).toObservable(),
                        animeUltimeService.getAnimeDetails(id).toObservable()
                ), {
                    val summary = it[0] as AnimeSummary
                    @Suppress("UNCHECKED_CAST")
                    val episodes = it[1] as List<Episode>
                    val details = it[2] as AnimeDetails
                    animeCombiner(summary, episodes, details)
                })
    }

    private fun animeCombiner(summary: AnimeSummary, episodes: List<Episode>,
                              details: AnimeDetails) : Anime {
        return Anime(
                id = summary.id,
                title = summary.title,
                imageUrl = summary.imageUrl,
                type = summary.type,
                synopsis = details.synopsis,
                totalCount = summary.totalCount,
                rating = summary.rating,
                episodes = episodes,
                productionYears = details.productionYears,
                studios = details.studios,
                genres = details.genres,
                author = details.author
        )
    }
}