package blue.aodev.animeultimetv.domain.model

data class Playlist(
        val title: String,
        val index: Int,
        val videos: List<Video>
) : android.os.Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = blue.aodev.animeultimetv.extensions.createParcel { Playlist(it) }
    }

    private constructor(parcelIn: android.os.Parcel) : this(
            parcelIn.readString(),
            parcelIn.readInt(),
            mutableListOf<Video>().apply {
                parcelIn.readTypedList(this, Video.CREATOR)
            }
    )

    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeInt(index)
        dest.writeTypedList(videos)
    }

    override fun describeContents() = 0

    val currentVideo: Video
        get() = videos[index]

    val hasNextVideo: Boolean
        get() = index < videos.size - 1
}