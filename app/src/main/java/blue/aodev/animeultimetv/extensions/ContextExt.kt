package blue.aodev.animeultimetv.extensions

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import blue.aodev.animeultimetv.R

fun Context.formatEpisodeDuration(duration: Int): String {
    val minutes = duration / 60
    val seconds = duration % 60
    return getString(R.string.episode_duration, minutes, seconds)
}

fun Context.getColorCompat(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)