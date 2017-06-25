package blue.aodev.animeultimetv.presentation.common

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.AnimeInfo

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(viewHolder: AbstractDetailsDescriptionPresenter.ViewHolder,
                                   item: Any?) {
        if (item == null) return
        val anime = item as AnimeInfo

        val resources = viewHolder.view.context.resources

        viewHolder.title.text = anime.title
        viewHolder.subtitle.text = resources.getQuantityString(
                R.plurals.details_episodesCount, anime.availableCount, anime.availableCount)
        viewHolder.body.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tempus lectus id porta molestie. Sed tincidunt tincidunt eros quis vulputate. Praesent vel venenatis neque. Sed hendrerit, nulla nec gravida suscipit, magna est tempor orci, eu efficitur nulla augue nec nibh. Vestibulum porttitor risus at lacus malesuada maximus. Ut eu posuere metus."
    }
}