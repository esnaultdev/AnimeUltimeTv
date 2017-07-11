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

    private val allAnimesSubject = BehaviorSubject.createDefault<List<AnimeSummary>>(emptyList())

    init {
        animeUltimeService.getAllAnimes()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .subscribeBy(
                        onNext = { allAnimesSubject.onNext(it) },
                        onError = { allAnimesSubject.onError(it) }
                        // Ignore the onComplete as we do not want the subject to complete
                )
    }

    override fun getAnimes(): Observable<List<AnimeSummary>> {
        return allAnimesSubject
    }

    override fun getAnimes(ids: IntArray): Observable<List<AnimeSummary>> {
        return allAnimesSubject.map { it.filter { it.id in ids } }
    }

    override fun search(query: String): Observable<List<AnimeSummary>> {
        return allAnimesSubject.map { it.filter { it.title.startsWith(query, true) } }
    }

    override fun getAnimeSummary(id: Int): Observable<AnimeSummary> {
        return allAnimesSubject.map { it.find { it.id == id } }
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

    override fun getTopAnimes(): Observable<List<AnimeSummary>> {
        return animeUltimeService.getTopAnimes()
                .flatMapObservable { getAnimes(it.map { it.id }.toIntArray()) }
    }
}