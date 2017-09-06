package blue.aodev.animeultimetv.presentation.common

import android.content.Context
import blue.aodev.animeultimetv.R


class MainAnimeCardPresenter : AnimeCardPresenter() {

    override fun initResources(context: Context) {
        super.initResources(context)

        val res = context.resources
        cardImageWidth = res.getDimensionPixelSize(R.dimen.main_card_width)
        cardImageHeight = res.getDimensionPixelSize(R.dimen.main_card_height)
    }
}