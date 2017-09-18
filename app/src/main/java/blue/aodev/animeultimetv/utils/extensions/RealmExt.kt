package blue.aodev.animeultimetv.utils.extensions

import io.realm.Realm
import io.realm.RealmObject


inline fun <reified T : RealmObject> Realm.loadOrCreate(primaryKey: String,
                                                        primaryKeyFieldName: String): T {
    val value = where(T::class.java).equalTo(primaryKeyFieldName, primaryKey).findFirst()
    return value ?: createObject(T::class.java, primaryKey)
}