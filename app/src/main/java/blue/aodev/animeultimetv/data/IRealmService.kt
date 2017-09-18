package blue.aodev.animeultimetv.data

import io.realm.Realm

interface IRealmService {
    fun getInstance(): Realm
}