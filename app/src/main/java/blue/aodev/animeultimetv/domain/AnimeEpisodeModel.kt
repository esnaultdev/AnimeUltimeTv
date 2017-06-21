package com.kingofgranges.max.animeultimetv.domain

import android.os.Parcel
import android.os.Parcelable
import com.kingofgranges.max.animeultimetv.extensions.createParcel

data class AnimeEpisodeModel(
        val title : String,
        val link : String
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = createParcel { AnimeEpisodeModel(it) }
    }

    private constructor(parcelIn: Parcel) : this(
            parcelIn.readString(),
            parcelIn.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(link)
    }

    override fun describeContents() = 0
}
