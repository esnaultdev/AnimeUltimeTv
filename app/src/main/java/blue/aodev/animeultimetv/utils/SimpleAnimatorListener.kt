package blue.aodev.animeultimetv.utils

import android.animation.Animator

interface SimpleAnimatorListener: Animator.AnimatorListener {

    override fun onAnimationStart(p0: Animator) {}

    override fun onAnimationEnd(p0: Animator) {}

    override fun onAnimationCancel(p0: Animator) {}

    override fun onAnimationRepeat(p0: Animator) {}
}