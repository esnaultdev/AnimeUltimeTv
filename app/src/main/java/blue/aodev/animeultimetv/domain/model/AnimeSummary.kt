package blue.aodev.animeultimetv.domain.model

import android.os.Parcel
import android.os.Parcelable
import blue.aodev.animeultimetv.utils.extensions.createParcel

data class AnimeSummary(
        val id: Int,
        val title: String,
        val imageUrl: String?,
        val type: AnimeType,
        val availableCount: Int,
        val totalCount: Int,
        val rating: Float
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = createParcel { AnimeSummary(it) }
    }

    private constructor(parcelIn: Parcel) : this(
            parcelIn.readInt(),
            parcelIn.readString(),
            parcelIn.readString(),
            AnimeType.values()[parcelIn.readInt()],
            parcelIn.readInt(),
            parcelIn.readInt(),
            parcelIn.readFloat()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(imageUrl)
        dest.writeInt(type.ordinal)
        dest.writeInt(availableCount)
        dest.writeInt(totalCount)
        dest.writeFloat(rating)
    }

    override fun describeContents() = 0
}