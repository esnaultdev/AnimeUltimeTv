package blue.aodev.animeultimetv.presentation.screen.main

import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.HeaderItem
import android.support.v17.leanback.widget.ListRow
import android.support.v17.leanback.widget.ListRowPresenter
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.model.EpisodeReleaseSummary
import blue.aodev.animeultimetv.extensions.fromBgToUi
import blue.aodev.animeultimetv.extensions.getColorCompat
import blue.aodev.animeultimetv.presentation.screen.animedetails.AnimeDetailsActivity
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.common.AnimeCardPresenter
import blue.aodev.animeultimetv.presentation.common.EpisodeReleaseCardPresenter
import blue.aodev.animeultimetv.presentation.common.LoadingObjectAdapter
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MainFragment : BrowseFragment() {

    companion object {
        private const val ROW_INDEX_TOP_ANIMES = 0
        private const val ROW_INDEX_RECENT_EPISODES = 1
    }

    @Inject
    lateinit var animeRepository: AnimeRepository

    private lateinit var categoryRowAdapter: ArrayObjectAdapter
    private lateinit var topAnimesAdapter: ArrayObjectAdapter
    private lateinit var recentEpisodesAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.graph.inject(this)

        animeRepository.getTopAnimes()
                .fromBgToUi()
                .subscribeBy(
                        onNext = { showTopAnimes(it) }
                )

        animeRepository.getRecentEpisodes()
                .fromBgToUi()
                .subscribeBy(
                        onNext = { showRecentEpisodes(it) }
                )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnSearchClickedListener {
            activity.onSearchRequested()
        }

        categoryRowAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = categoryRowAdapter

        setOnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                is AnimeSummary -> showAnimeDetails(item)
                is EpisodeReleaseSummary -> showAnimeDetails(item.animeSummary)
            }
        }

        createEmptyRows()

        brandColor = activity.getColorCompat(R.color.colorPrimaryDark)
    }

    private fun createEmptyRows() {
        showTopAnimesLoading()
        showRecentEpisodesLoading()
    }

    //region Top animes

    private val topAnimesHeader: HeaderItem by lazy {
        HeaderItem(getString(R.string.main_topAnimes_title))
    }

    private fun showTopAnimesLoading() {
        val row = ListRow(topAnimesHeader, LoadingObjectAdapter())
        categoryRowAdapter.add(ROW_INDEX_TOP_ANIMES, row)
    }

    private fun showTopAnimes(topAnimes: List<AnimeSummary>) {
        val presenter = AnimeCardPresenter()
        topAnimesAdapter = ArrayObjectAdapter(presenter)
        val row = ListRow(topAnimesHeader, topAnimesAdapter)

        topAnimesAdapter.addAll(0, topAnimes)
        categoryRowAdapter.replace(ROW_INDEX_TOP_ANIMES, row)
    }

    //endregion

    //region Recent episodes

    private val recentEpisodesHeader: HeaderItem by lazy {
        HeaderItem(getString(R.string.main_recentEpisodes_title))
    }

    private fun showRecentEpisodesLoading() {
        val row = ListRow(recentEpisodesHeader, LoadingObjectAdapter())
        categoryRowAdapter.add(ROW_INDEX_RECENT_EPISODES, row)
    }

    private fun showRecentEpisodes(recentEpisodes: List<EpisodeReleaseSummary>) {
        val presenter = EpisodeReleaseCardPresenter()
        recentEpisodesAdapter = ArrayObjectAdapter(presenter)
        val row = ListRow(recentEpisodesHeader, recentEpisodesAdapter)

        recentEpisodesAdapter.addAll(0, recentEpisodes)
        categoryRowAdapter.replace(ROW_INDEX_RECENT_EPISODES, row)
    }

    //endregion

    private fun showAnimeDetails(anime: AnimeSummary) {
        val intent = AnimeDetailsActivity.getIntent(activity, anime)
        activity.startActivity(intent)
    }
}