package blue.aodev.animeultimetv.presentation.common

import android.content.Context
import android.support.v17.leanback.widget.ImageCardView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.model.AnimeType
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class AnimeCardPresenter(cardImageWidth: Int, cardImageHeight: Int)
    : BaseCardPresenter(cardImageWidth, cardImageHeight) {

    companion object {
        fun forMain(context: Context): AnimeCardPresenter {
            val res = context.resources
            return AnimeCardPresenter(
                res.getDimensionPixelSize(R.dimen.main_card_width),
                res.getDimensionPixelSize(R.dimen.main_card_height)
            )
        }

        fun default(context: Context): AnimeCardPresenter {
            val res = context.resources
            return AnimeCardPresenter(
                    res.getDimensionPixelSize(R.dimen.anime_card_width),
                    res.getDimensionPixelSize(R.dimen.anime_card_height)
            )
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val animeInfo = item as AnimeSummary
        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = animeInfo.title

        // TODO Have this mapping against string resources
        cardView.contentText = when (animeInfo.type) {
            AnimeType.ANIME -> "Anime"
            AnimeType.OAV -> "OAV"
            AnimeType.MOVIE -> "Film"
        }

        if (animeInfo.imageUrl != null) {
            Glide.with(cardView.context)
                    .load(animeInfo.imageUrl)
                    .apply(RequestOptions.centerCropTransform())
                    .into(cardView.mainImageView)
        }
    }
}