package blue.aodev.animeultimetv.presentation.animedetails

import android.support.v17.leanback.widget.DetailsOverviewLogoPresenter
import android.support.v17.leanback.widget.DetailsOverviewRow
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter
import android.support.v17.leanback.widget.Presenter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import blue.aodev.animeultimetv.R

class AnimeDetailsOverviewLogoPresenter : DetailsOverviewLogoPresenter() {

    internal class ViewHolder(view: View) : DetailsOverviewLogoPresenter.ViewHolder(view) {

        override fun getParentPresenter(): FullWidthDetailsOverviewRowPresenter {
            return mParentPresenter
        }

        override fun getParentViewHolder(): FullWidthDetailsOverviewRowPresenter.ViewHolder {
            return mParentViewHolder
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val imageView = LayoutInflater.from(parent.context)
                .inflate(R.layout.lb_fullwidth_details_overview_logo, parent, false) as ImageView

        val res = parent.resources
        val width = res.getDimensionPixelSize(R.dimen.detailsFragment_thumbnail_width)
        val height = res.getDimensionPixelSize(R.dimen.detailsFragment_thumbnail_height)
        imageView.layoutParams = ViewGroup.MarginLayoutParams(width, height)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        return ViewHolder(imageView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val row = item as DetailsOverviewRow
        val imageView = viewHolder.view as ImageView
        imageView.setImageDrawable(row.imageDrawable)
        if (isBoundToImage(viewHolder as ViewHolder, row)) {
            val vh = viewHolder
            vh.parentPresenter.notifyOnBindLogo(vh.parentViewHolder)
        }
    }
}