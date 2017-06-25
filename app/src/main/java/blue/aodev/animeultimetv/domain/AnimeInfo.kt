package blue.aodev.animeultimetv.domain

import android.os.Parcel
import android.os.Parcelable
import blue.aodev.animeultimetv.extensions.createParcel

data class AnimeInfo(
        val id: Int,
        val title: String,
        val imageUrl: String?,
        val type: AnimeInfoType,
        val availableCount: Int,
        val totalCount: Int,
        val rating: Double // TODO make it a Float
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = createParcel { AnimeInfo(it) }
    }

    private constructor(parcelIn: Parcel) : this(
            parcelIn.readInt(),
            parcelIn.readString(),
            parcelIn.readString(),
            AnimeInfoType.values()[parcelIn.readInt()],
            parcelIn.readInt(),
            parcelIn.readInt(),
            parcelIn.readDouble()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(imageUrl)
        dest.writeInt(type.ordinal)
        dest.writeInt(availableCount)
        dest.writeInt(totalCount)
        dest.writeDouble(rating)
    }

    override fun describeContents() = 0
}

enum class AnimeInfoType { ANIME, OAV, MOVIE }