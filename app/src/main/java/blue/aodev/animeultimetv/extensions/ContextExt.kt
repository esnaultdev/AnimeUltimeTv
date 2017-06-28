package blue.aodev.animeultimetv.extensions

import android.content.Context

fun Context.formatEpisodeDuration(duration: Int): String {
    val minutes = duration / 60
    val seconds = duration % 60
    return "$minutes:$seconds"
}