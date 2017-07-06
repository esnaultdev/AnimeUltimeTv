package blue.aodev.animeultimetv.presentation.animedetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v17.leanback.app.DetailsFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.Anime
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.domain.model.Episode
import blue.aodev.animeultimetv.presentation.application.MyApplication
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

    private var anime: Anime? = null

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
                            this.anime = it
                            updateDetailsRow(it)
                            updateEpisodesRow(it.episodes)
                        }
                )

        // When an episode item is clicked.
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private fun setupAdapter() {
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(
                DetailsDescriptionPresenter(activity), AnimeDetailsOverviewLogoPresenter())

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
            if (item is Episode) {
                anime?.let { anime ->
                    val episodeIndex = anime.episodes.indexOf(item)
                    val intent = PlaybackActivity.getIntent(activity, anime, episodeIndex)
                    activity.startActivity(intent)
                }
            }
        }
    }
}