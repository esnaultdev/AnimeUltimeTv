package blue.aodev.animeultimetv.utils.extensions

import android.os.Parcel
import android.os.Parcelable

/**
 * Not really an extension, but meh
 * Coming from
 * https://android.jlelse.eu/keddit-part-8-orientation-change-with-kotlin-parcelable-data-classes-f28136e8a6a8
 */
inline fun <reified T : Parcelable> createParcel(
        crossinline createFromParcel: (Parcel) -> T?): Parcelable.Creator<T> =
        object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel): T? = createFromParcel(source)
            override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
        }
