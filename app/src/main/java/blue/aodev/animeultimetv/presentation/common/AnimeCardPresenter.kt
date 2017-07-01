package blue.aodev.animeultimetv.presentation.common

import android.content.Context
import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.AnimeInfo
import blue.aodev.animeultimetv.domain.AnimeInfoType
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class AnimeCardPresenter : Presenter() {

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

        defaultBackgroundColor = ContextCompat.getColor(context, R.color.default_background)
        selectedBackgroundColor = ContextCompat.getColor(context, R.color.selected_background)

        val res = context.resources
        cardImageWidth = res.getDimensionPixelSize(R.dimen.anime_card_width)
        cardImageHeight = res.getDimensionPixelSize(R.dimen.anime_card_height)
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) selectedBackgroundColor else defaultBackgroundColor

        // Both background colors should be set because the view's
        // background is temporarily visible during animations.
        view.setBackgroundColor(color)
        view.findViewById<View>(R.id.info_field).setBackgroundColor(color)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val animeInfo = item as AnimeInfo

        val cardView = viewHolder.view as ImageCardView
        cardView.titleText = animeInfo.title

        // TODO Have this mapping against string resources
        cardView.contentText = when (animeInfo.type) {
            AnimeInfoType.ANIME -> "Anime"
            AnimeInfoType.OAV -> "OAV"
            AnimeInfoType.MOVIE -> "Film"
        }

        if (animeInfo.imageUrl != null) {
            Glide.with(cardView.context)
                    .load(animeInfo.imageUrl)
                    .apply(RequestOptions.centerCropTransform())
                    .into(cardView.mainImageView)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView

        // Remove references to images so that the garbage collector can free up memory.
        cardView.badgeImage = null
        cardView.mainImage = null
    }
}