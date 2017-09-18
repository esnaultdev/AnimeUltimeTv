package blue.aodev.animeultimetv.data.local

import blue.aodev.animeultimetv.domain.model.Timing
import io.reactivex.Completable
import io.reactivex.Single

interface ILocalSourceSet {

    fun saveTiming(timing: Timing): Completable

    fun getTimings(): Single<List<Timing>>
}