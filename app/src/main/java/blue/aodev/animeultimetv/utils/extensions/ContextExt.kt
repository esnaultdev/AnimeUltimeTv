package blue.aodev.animeultimetv.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import blue.aodev.animeultimetv.R

fun Context.formatEpisodeDuration(duration: Int): String {
    val minutes = duration / 60
    val seconds = duration % 60
    return getString(R.string.episode_duration, minutes, seconds)
}

fun Context.getColorCompat(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable {
    return ContextCompat.getDrawable(this, drawableRes)
}