package blue.aodev.animeultimetv.presentation.common

import android.support.v17.leanback.widget.ImageCardView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.EpisodeReleaseSummary
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class EpisodeReleaseCardPresenter : BaseCardPresenter() {

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