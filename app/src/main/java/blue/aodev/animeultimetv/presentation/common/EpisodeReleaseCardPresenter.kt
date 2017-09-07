package blue.aodev.animeultimetv.presentation.common

import android.content.Context
import android.support.v17.leanback.widget.ImageCardView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.EpisodeReleaseSummary
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class EpisodeReleaseCardPresenter(cardImageWidth: Int, cardImageHeight: Int)
    : BaseCardPresenter(cardImageWidth, cardImageHeight) {

    companion object {
        fun forMain(context: Context): EpisodeReleaseCardPresenter {
            val res = context.resources
            return EpisodeReleaseCardPresenter(
                    res.getDimensionPixelSize(R.dimen.main_card_width),
                    res.getDimensionPixelSize(R.dimen.main_card_height)
            )
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val context = viewHolder.view.context
        val episodeRelease = item as EpisodeReleaseSummary
        val animeInfo = episodeRelease.animeSummary
        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = animeInfo.title

        cardView.contentText =
                context.getString(R.string.common_episodeRelease_numbers, episodeRelease.episodes)

        if (animeInfo.imageUrl != null) {
            Glide.with(cardView.context)
                    .load(animeInfo.imageUrl)
                    .apply(RequestOptions.centerCropTransform())
                    .into(cardView.mainImageView)
        }
    }
}