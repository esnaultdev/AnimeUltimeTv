package blue.aodev.animeultimetv.data

import blue.aodev.animeultimetv.domain.AnimeInfo
import blue.aodev.animeultimetv.domain.AnimeUltimeRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class AnimeUltimeRepositoryImpl : AnimeUltimeRepository {

    private val behaviorSubject = BehaviorSubject.createDefault<List<AnimeInfo>>(emptyList())

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(HttpUrl.parse("http://www.anime-ultime.net/")!!)
                .addConverterFactory(AnimeInfoAdapter.FACTORY)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val AUService = retrofit.create<HtmlAUService>(HtmlAUService::class.java)

        AUService.getAllAnimes()
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

}