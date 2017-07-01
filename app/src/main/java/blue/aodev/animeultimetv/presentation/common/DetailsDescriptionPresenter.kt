package blue.aodev.animeultimetv.presentation.common

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.Anime
import blue.aodev.animeultimetv.domain.AnimeSummary

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(viewHolder: AbstractDetailsDescriptionPresenter.ViewHolder,
                                   item: Any?) {
        if (item == null) return

        val resources = viewHolder.view.context.resources

        var title = ""
        var episodeCount = 0
        var synopsis = ""

        if (item is AnimeSummary) {
            title = item.title
            episodeCount = item.availableCount
        } else if (item is Anime) {
            title = item.title
            episodeCount = item.episodes.size
            synopsis = item.synopsis
        }

        viewHolder.title.text = title
        viewHolder.subtitle.text = resources.getQuantityString(
                R.plurals.details_episodesCount, episodeCount, episodeCount)
        viewHolder.body.text = synopsis
        viewHolder.body.maxLines = 10
    }
}