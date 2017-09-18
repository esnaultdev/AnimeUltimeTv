package blue.aodev.animeultimetv.data.local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TimingRealm : RealmObject() {

    @PrimaryKey
    lateinit var primaryKey: String
    var time: Long = 0

    companion object {
        fun toPrimaryKey(animeId: Int, episodeIndex: Int): String {
            return "$animeId:$episodeIndex"
        }

        fun fromPrimaryKey(primaryKey: String): Pair<Int, Int> {
            val split = primaryKey.split(":")
            return split.first().toInt() to split.last().toInt()
        }
    }
}