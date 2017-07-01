package blue.aodev.animeultimetv.presentation.animedetails

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v17.leanback.app.DetailsFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.Anime
import blue.aodev.animeultimetv.domain.AnimeSummary
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.domain.Episode
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.common.DetailsDescriptionPresenter
import blue.aodev.animeultimetv.presentation.common.EpisodeCardPresenter
import blue.aodev.animeultimetv.presentation.playback.PlaybackActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AnimeDetailsFragment : DetailsFragment() {

    companion object {
        val ACTION_EPISODES = 1
    }

    @Inject
    lateinit var animeRepository: AnimeRepository

    private lateinit var globalAdapter: ArrayObjectAdapter
    private lateinit var presenterSelector: ClassPresenterSelector
    private lateinit var detailsRow: DetailsOverviewRow
    private var episodeAdapter = ArrayObjectAdapter(EpisodeCardPresenter())

    val animeSummary: AnimeSummary by lazy {
        (activity as AnimeDetailsActivity).animeSummary
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.graph.inject(this)

        setupAdapter()
        setupEmptyDetailsRow()
        setupEmptyEpisodesRow()

        animeRepository.getAnime(animeSummary.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            updateDetailsRow(it)
                            updateEpisodesRow(it.episodes)
                        }
                )

        // When an episode item is clicked.
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private fun setupAdapter() {
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(
                DetailsDescriptionPresenter(), AnimeDetailsOverviewLogoPresenter())

        detailsPresenter.backgroundColor =
                ContextCompat.getColor(activity, R.color.selected_background)
        detailsPresenter.initialState = FullWidthDetailsOverviewRowPresenter.STATE_FULL
        detailsPresenter.headerPresenter = null

        detailsPresenter.onActionClickedListener = OnActionClickedListener { action ->
            if (action.id == ACTION_EPISODES.toLong()) {
                setSelectedPosition(1)
            }
        }

        presenterSelector = ClassPresenterSelector()
        presenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
        presenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
        globalAdapter = ArrayObjectAdapter(presenterSelector)
        adapter = globalAdapter
    }

    internal class AnimeDetailsOverviewLogoPresenter : DetailsOverviewLogoPresenter() {

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

    private fun setupEmptyDetailsRow() {
        detailsRow = DetailsOverviewRow(animeSummary)

        Glide.with(this)
                .load(animeSummary.imageUrl)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable?,
                                                 transition: Transition<in Drawable>?) {
                        detailsRow.imageDrawable = resource
                    }
                })

        val adapter = SparseArrayObjectAdapter()
        adapter.set(ACTION_EPISODES, Action(ACTION_EPISODES.toLong(),
                resources.getString(R.string.details_action_episodes)))
        detailsRow.actionsAdapter = adapter

        globalAdapter.add(detailsRow)
    }

    private fun updateDetailsRow(anime: Anime) {
        detailsRow.item = anime
    }

    private fun setupEmptyEpisodesRow() {
        val header = setupEpisodeListRowHeader()
        globalAdapter.add(ListRow(header, episodeAdapter))
    }

    private fun updateEpisodesRow(episodes: List<Episode>) {
        episodeAdapter.clear()
        episodeAdapter.addAll(0, episodes)
    }

    private fun setupEpisodeListRowHeader(): HeaderItem {
        val subcategories = arrayOf(getString(R.string.details_episodesTitle))
        return HeaderItem(0, subcategories[0])
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(itemViewHolder: Presenter.ViewHolder, item: Any,
                                   rowViewHolder: RowPresenter.ViewHolder, row: Row) {
            val intent = Intent(activity, PlaybackActivity::class.java)
            activity.startActivity(intent)
        }
    }
}