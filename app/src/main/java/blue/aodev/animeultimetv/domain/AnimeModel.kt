package com.kingofgranges.max.animeultimetv.domain

import android.os.Parcel
import android.os.Parcelable
import com.kingofgranges.max.animeultimetv.extensions.createParcel

data class AnimeModel(
        val title: String,
        val image: String,
        val synopsis: String,
        val episodes: List<AnimeEpisodeModel>
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = createParcel { AnimeModel(it) }
    }

    private constructor(parcelIn: Parcel) : this(
            parcelIn.readString(),
            parcelIn.readString(),
            parcelIn.readString(),
            mutableListOf<AnimeEpisodeModel>().apply {
                parcelIn.readTypedList(this, AnimeEpisodeModel.CREATOR)
            }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(image)
        dest.writeString(synopsis)
        dest.writeTypedList(episodes)
    }

    override fun describeContents() = 0
}
