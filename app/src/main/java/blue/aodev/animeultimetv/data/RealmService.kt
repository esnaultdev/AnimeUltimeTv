package blue.aodev.animeultimetv.data

import io.realm.Realm


class RealmService(): IRealmService {
    override fun getInstance(): Realm {
        return Realm.getDefaultInstance()
    }
}