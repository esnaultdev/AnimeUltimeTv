package blue.aodev.animeultimetv.data

import blue.aodev.animeultimetv.data.model.AnimeDetails
import blue.aodev.animeultimetv.data.model.EpisodeReleaseId
import blue.aodev.animeultimetv.domain.model.Anime
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.domain.model.Episode
import blue.aodev.animeultimetv.domain.model.EpisodeReleaseSummary
import blue.aodev.animeultimetv.utils.extensions.mapOf
import blue.aodev.animeultimetv.utils.extensions.sliceIfPresent
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AnimeUltimeRepository(val animeUltimeService: AnimeUltimeService) : AnimeRepository {

    private val historyPeriodFormat: DateFormat by lazy {
        SimpleDateFormat("MMyyyy")
    }

    private val allAnimesSubject = BehaviorSubject.createDefault<Map<Int, AnimeSummary>>(emptyMap())

    init {
        animeUltimeService.getAllAnimes()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .subscribeBy(
                        onNext = { allAnimesSubject.onNext(convertAnimesToMap(it)) },
                        onError = { allAnimesSubject.onError(it) }
                        // Ignore the onComplete as we do not want the subject to complete
                )
    }

    private fun convertAnimesToMap(animes: List<AnimeSummary>): Map<Int, AnimeSummary> {
        return mapOf(animes.map { it.id to it })
    }

    override fun isInitialized(): Observable<Boolean> {
        return allAnimesSubject.map { it.isNotEmpty() }
    }

    override fun getAnimes(ids: List<Int>): Observable<List<AnimeSummary>> {
        return allAnimesSubject.map { it.sliceIfPresent(ids) }
    }

    override fun search(query: String): Observable<List<AnimeSummary>> {
        return allAnimesSubject
                .map { it.values }
                .map {
                    it.filter { it.title.startsWith(query, true) }
                            .sortedBy { it.title }
                }
    }

    override fun getAnimeSummary(id: Int): Observable<AnimeSummary> {
        return allAnimesSubject.map { it.getValue(id) }
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
                .flatMapObservable { getAnimes(it.map { it.id }) }
    }

    override fun getRecentEpisodes(): Observable<List<EpisodeReleaseSummary>> {
        val calendar = Calendar.getInstance(Locale.FRANCE)
        val currentDate = calendar.time
        calendar.add(Calendar.MONTH, -1)
        val previousMonthDate = calendar.time

        return Single.zip(
                animeUltimeService.getEpisodesHistory(historyPeriodFormat.format(currentDate)),
                animeUltimeService.getEpisodesHistory(historyPeriodFormat.format(previousMonthDate)),
                BiFunction<List<EpisodeReleaseId>, List<EpisodeReleaseId>, List<EpisodeReleaseId>> {
                    t1, t2 -> t1 + t2
                })
                .flatMapObservable { getEpisodeReleaseSummary(it) }
    }

    private fun getEpisodeReleaseSummary(releaseIds: List<EpisodeReleaseId>): Observable<List<EpisodeReleaseSummary>> {
        return allAnimesSubject.map { animes ->
            val result = ArrayList<EpisodeReleaseSummary>(releaseIds.size)
            releaseIds.forEach {
                val anime = animes[it.animeId]
                if (anime != null) result.add(EpisodeReleaseSummary(anime, it.numbers))
            }
            result
        }
    }
}