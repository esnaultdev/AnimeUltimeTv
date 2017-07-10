package blue.aodev.animeultimetv.presentation.common

import android.content.Context
import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.Episode
import blue.aodev.animeultimetv.extensions.formatEpisodeDuration
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class EpisodeCardPresenter : Presenter() {

    private var selectedBackgroundColor = -1
    private var defaultBackgroundColor = -1
    private var cardImageWidth = -1
    private var cardImageHeight = -1

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        initResources(parent.context)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true

        // Setup image
        cardView.setMainImageDimensions(cardImageWidth, cardImageHeight)
        cardView.setMainImageScaleType(ImageView.ScaleType.FIT_XY)

        updateCardBackgroundColor(cardView, false)
        return ViewHolder(cardView)
    }

    private fun initResources(context: Context) {
        if (defaultBackgroundColor != -1) return

        defaultBackgroundColor = ContextCompat.getColor(context, R.color.card_background_default)
        selectedBackgroundColor = ContextCompat.getColor(context, R.color.card_background_selected)

        val res = context.resources
        cardImageWidth = res.getDimensionPixelSize(R.dimen.episode_card_width)
        cardImageHeight = res.getDimensionPixelSize(R.dimen.episode_card_height)
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) selectedBackgroundColor else defaultBackgroundColor

        // Both background colors should be set because the view's
        // background is temporarily visible during animations.
        view.setBackgroundColor(color)
        view.findViewById<View>(R.id.info_field).setBackgroundColor(color)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val context = viewHolder.view.context

        val episode = item as Episode

        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = context.getString(R.string.episode_title, episode.number)
        cardView.contentText = context.formatEpisodeDuration(episode.duration)

        Glide.with(context)
                .load(episode.imageUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(cardView.mainImageView)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView

        // Remove references to images so that the garbage collector can free up memory.
        cardView.badgeImage = null
        cardView.mainImage = null
    }
}