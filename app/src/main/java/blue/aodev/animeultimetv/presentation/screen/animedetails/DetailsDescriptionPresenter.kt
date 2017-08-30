package blue.aodev.animeultimetv.presentation.screen.animedetails

import android.content.Context
import android.support.v17.leanback.widget.Presenter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.Anime
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import butterknife.BindView
import butterknife.ButterKnife

class DetailsDescriptionPresenter(private val context: Context) : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_details_description, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        (viewHolder as ViewHolder).populate(item)
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        // Nothing to do here.
    }

    inner class ViewHolder(view: View): Presenter.ViewHolder(view) {
        @BindView(R.id.primary_text)
        lateinit var primaryText: TextView
        @BindView(R.id.secondary_text_first)
        lateinit var secondaryText1: TextView
        @BindView(R.id.secondary_text_second)
        lateinit var secondaryText2: TextView
        @BindView(R.id.extra_text)
        lateinit var extraText: TextView
        @BindView(R.id.loading_progress)
        lateinit var loadingProgress: ProgressBar

        init {
            ButterKnife.bind(this, view)
        }

        fun populate(item: Any?) {
            if (item == null) return

            val resources = context.resources

            var title = ""
            var episodeCount = 0

            if (item is AnimeSummary) {
                loadingProgress.visibility = View.VISIBLE
                title = item.title
                episodeCount = item.availableCount
            } else if (item is Anime) {
                loadingProgress.visibility = View.GONE
                title = item.title
                episodeCount = item.episodes.size

                extraText.text = item.synopsis
                val yearString = with (item.productionYears) {
                    if (start == endInclusive) {
                        start.toString()
                    } else {
                        resources.getString(R.string.details_productionYears, start, endInclusive)
                    }
                }
                secondaryText2.text = yearString
            }

            primaryText.text = title
            secondaryText1.text = resources.getQuantityString(
                    R.plurals.details_episodesCount, episodeCount, episodeCount)
        }
    }
}