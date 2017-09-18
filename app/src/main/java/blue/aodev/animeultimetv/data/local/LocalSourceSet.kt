package blue.aodev.animeultimetv.data.local

import blue.aodev.animeultimetv.data.IRealmService
import blue.aodev.animeultimetv.domain.model.Timing
import blue.aodev.animeultimetv.utils.extensions.loadOrCreate
import io.reactivex.Completable
import io.reactivex.Single
import io.realm.Realm

class LocalSourceSet(val realm: IRealmService) : ILocalSourceSet {

    override fun saveTiming(timing: Timing): Completable {
        return Completable.fromAction {
            with(realm.getInstance()) {
                use {
                    executeTransaction {
                        val timingRealm = timing.toRealmObject(this)
                        copyToRealmOrUpdate(timingRealm)
                    }
                }
            }
        }
    }

    override fun getTimings(): Single<List<Timing>> {
        return Single.fromCallable {
            with(realm.getInstance()) {
                use {
                    val timings = where(TimingRealm::class.java).findAll()
                    copyFromRealm(timings).map { it.toModel() }
                }
            }
        }
    }


    fun Timing.toRealmObject(realm: Realm): TimingRealm {
        val primaryKey = TimingRealm.toPrimaryKey(animeId, episodeIndex)
        val timingRealm = realm.loadOrCreate<TimingRealm>(primaryKey, "primaryKey")
        timingRealm.time = time
        return timingRealm
    }

    fun TimingRealm.toModel(): Timing {
        val ids = TimingRealm.fromPrimaryKey(this.primaryKey)
        return Timing(ids.first, ids.second, this.time)
    }
}