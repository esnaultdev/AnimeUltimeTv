package blue.aodev.animeultimetv.domain.model

import android.os.Parcel
import android.os.Parcelable
import blue.aodev.animeultimetv.utils.extensions.createParcel

data class Video(
        val title: String,
        val videoUrl: String,
        val hdVideoUrl: String,
        val duration: Int
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = createParcel { Video(it) }
    }

    private constructor(parcelIn: Parcel) : this(
            parcelIn.readString(),
            parcelIn.readString(),
            parcelIn.readString(),
            parcelIn.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(videoUrl)
        dest.writeString(hdVideoUrl)
        dest.writeInt(duration)
    }

    override fun describeContents() = 0

}