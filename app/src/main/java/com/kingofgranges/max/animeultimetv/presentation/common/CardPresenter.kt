package com.kingofgranges.max.animeultimetv.presentation.common

import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kingofgranges.max.animeultimetv.R
import com.kingofgranges.max.animeultimetv.data.SearchNetworkModel

class CardPresenter : Presenter() {
    private var selectedBackgroundColor = -1
    private var defaultBackgroundColor = -1

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        defaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        selectedBackgroundColor = ContextCompat.getColor(parent.context, R.color.selected_background)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return Presenter.ViewHolder(cardView)
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) selectedBackgroundColor else defaultBackgroundColor

        // Both background colors should be set because the view's
        // background is temporarily visible during animations.
        view.setBackgroundColor(color)
        view.findViewById(R.id.info_field).setBackgroundColor(color)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val animeInfo = item as SearchNetworkModel

        val cardView = viewHolder.view as ImageCardView
        cardView.titleText = animeInfo.title
        cardView.contentText = animeInfo.type

        if (animeInfo.imageUrl != null) {
            // Set card size from dimension resources.
            val res = cardView.resources
            val width = res.getDimensionPixelSize(R.dimen.card_width)
            val height = res.getDimensionPixelSize(R.dimen.card_height)
            cardView.setMainImageDimensions(width, height)

            Glide.with(cardView.context)
                    .load(animeInfo.imageUrl)
                    .into(cardView.mainImageView)
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val cardView = viewHolder.view as ImageCardView

        // Remove references to images so that the garbage collector can free up memory.
        cardView.badgeImage = null
        cardView.mainImage = null
    }
}