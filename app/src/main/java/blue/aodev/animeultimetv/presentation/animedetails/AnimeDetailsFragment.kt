package blue.aodev.animeultimetv.presentation.animedetails

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
import blue.aodev.animeultimetv.data.EpisodeInfo
import blue.aodev.animeultimetv.domain.AnimeInfo
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.common.DetailsDescriptionPresenter
import blue.aodev.animeultimetv.presentation.common.EpisodeCardPresenter
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
    private var episodeAdapter = ArrayObjectAdapter(EpisodeCardPresenter())

    val animeInfo: AnimeInfo by lazy {
        (activity as AnimeDetailsActivity).animeInfo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.graph.inject(this)

        setupAdapter()
        setupDetailsOverviewRow()
        setupEmptyEpisodeListRow()

        animeRepository.getEpisodesInfo(animeInfo.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { updateEpisodes(it) }
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

    private fun setupDetailsOverviewRow() {
        val row = DetailsOverviewRow(animeInfo)

        Glide.with(this)
                .load(animeInfo.imageUrl)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable?,
                                                 transition: Transition<in Drawable>?) {
                        row.imageDrawable = resource
                    }
                })

        val adapter = SparseArrayObjectAdapter()

        adapter.set(ACTION_EPISODES, Action(ACTION_EPISODES.toLong(),
                resources.getString(R.string.details_action_episodes)))
        row.actionsAdapter = adapter

        globalAdapter.add(row)
    }

    private fun setupEmptyEpisodeListRow() {
        val header = setupEpisodeListRowHeader()
        globalAdapter.add(ListRow(header, episodeAdapter))
    }

    private fun updateEpisodes(episodes: List<EpisodeInfo>) {
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

            // TODO check if it is an episode, and open the video associated with it

        }
    }
}