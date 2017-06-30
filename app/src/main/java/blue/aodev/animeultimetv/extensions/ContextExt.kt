package blue.aodev.animeultimetv.extensions

import android.content.Context
import blue.aodev.animeultimetv.R

fun Context.formatEpisodeDuration(duration: Int): String {
    val minutes = duration / 60
    val seconds = duration % 60
    return getString(R.string.episode_duration, minutes, seconds)
}