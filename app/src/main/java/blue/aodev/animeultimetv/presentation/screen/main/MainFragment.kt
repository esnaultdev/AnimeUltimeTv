package blue.aodev.animeultimetv.presentation.screen.main

import android.app.Fragment
import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.FocusHighlight
import android.support.v17.leanback.widget.HeaderItem
import android.support.v17.leanback.widget.ListRowPresenter
import android.support.v17.leanback.widget.PageRow
import android.support.v17.leanback.widget.Row
import android.support.v17.leanback.widget.VerticalGridPresenter
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.model.EpisodeReleaseSummary
import blue.aodev.animeultimetv.utils.extensions.fromBgToUi
import blue.aodev.animeultimetv.utils.extensions.getColorCompat
import blue.aodev.animeultimetv.presentation.screen.animedetails.AnimeDetailsActivity
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.common.AnimeCardPresenter
import blue.aodev.animeultimetv.presentation.common.EpisodeReleaseCardPresenter
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


class MainFragment : BrowseFragment() {

    companion object {
        private val HEADER_ID_TOP_ANIMES: Long = 1
        private val HEADER_ID_RECENT_EPISODES: Long = 2
    }

    @Inject
    lateinit var animeRepository: AnimeRepository

    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.graph.inject(this)

        setupUi()
        loadData()

        mainFragmentRegistry.registerFragment(
                PageRow::class.java, PageRowFragmentFactory(animeRepository))
    }

    private fun setupUi() {
        headersState = BrowseFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = activity.getColorCompat(R.color.colorPrimaryDark)

        setOnSearchClickedListener {
            activity.onSearchRequested()
        }
    }

    private fun loadData() {
        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = rowsAdapter

        createRows()
    }

    private fun createRows() {
        val topAnimesTitle = getString(R.string.main_topAnimes_title)
        val headerItemTopAnimes = HeaderItem(HEADER_ID_TOP_ANIMES, topAnimesTitle)
        val pageRowTopAnimes = PageRow(headerItemTopAnimes)
        rowsAdapter.add(pageRowTopAnimes)

        val recentEpisodesTitle = getString(R.string.main_recentEpisodes_title)
        val headerItemRecentEpisodes = HeaderItem(HEADER_ID_RECENT_EPISODES, recentEpisodesTitle)
        val pageRowRecentEpisodes = PageRow(headerItemRecentEpisodes)
        rowsAdapter.add(pageRowRecentEpisodes)
    }

    private class PageRowFragmentFactory(
            private val animeRepository: AnimeRepository
    ) : BrowseFragment.FragmentFactory<Fragment>() {

        override fun createFragment(rowObj: Any): Fragment {
            val row = rowObj as Row
            if (row.headerItem.id == HEADER_ID_TOP_ANIMES) {
                return TopAnimesFragment(animeRepository)
            } else if (row.headerItem.id == HEADER_ID_RECENT_EPISODES) {
                return RecentEpisodesFragment(animeRepository)
            }

            throw IllegalArgumentException(String.format("Invalid row %s", rowObj))
        }
    }


    class TopAnimesFragment(private val animeRepository: AnimeRepository) : GridFragment() {

        companion object {
            private val COLUMNS = 4
            private val ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_SMALL
        }

        private lateinit var adapter: ArrayObjectAdapter
        private var disposable: Disposable? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setupAdapter()
            loadData()
            mainFragmentAdapter.fragmentHost.notifyDataReady(mainFragmentAdapter)
        }

        override fun onDestroy() {
            super.onDestroy()
            disposable?.let { if (!it.isDisposed) it.dispose() }
        }

        private fun setupAdapter() {
            val presenter = VerticalGridPresenter(ZOOM_FACTOR)
            presenter.numberOfColumns = COLUMNS
            gridPresenter = presenter

            val cardPresenter = AnimeCardPresenter.forMain(activity)
            adapter = ArrayObjectAdapter(cardPresenter)
            setAdapter(adapter)

            setOnItemViewClickedListener { _, item, _, _ ->
                if (item is AnimeSummary) {
                    showAnimeDetails(item)
                }
            }
        }

        private fun loadData() {
            disposable = animeRepository.getTopAnimes()
                    .fromBgToUi()
                    .subscribeBy(
                            onNext = { showData(it) },
                            onError = { /* TODO Display an error */ }
                    )
        }

        private fun showData(topAnimes: List<AnimeSummary>) {
            adapter.clear()
            adapter.addAll(0, topAnimes)
        }

        private fun showAnimeDetails(anime: AnimeSummary) {
            val intent = AnimeDetailsActivity.getIntent(activity, anime)
            activity.startActivity(intent)
        }
    }


    class RecentEpisodesFragment(private val animeRepository: AnimeRepository) : GridFragment() {

        companion object {
            private val COLUMNS = 4
            private val ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_SMALL
        }

        private lateinit var adapter: ArrayObjectAdapter
        private var disposable: Disposable? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setupAdapter()
            loadData()
            mainFragmentAdapter.fragmentHost.notifyDataReady(mainFragmentAdapter)
        }

        override fun onDestroy() {
            super.onDestroy()
            disposable?.let { if (!it.isDisposed) it.dispose() }
        }

        private fun setupAdapter() {
            val presenter = VerticalGridPresenter(ZOOM_FACTOR)
            presenter.numberOfColumns = COLUMNS
            gridPresenter = presenter

            val cardPresenter = EpisodeReleaseCardPresenter.forMain(activity)
            adapter = ArrayObjectAdapter(cardPresenter)
            setAdapter(adapter)

            setOnItemViewClickedListener { _, item, _, _ ->
                if (item is EpisodeReleaseSummary) {
                    showAnimeDetails(item.animeSummary)
                }
            }
        }

        private fun loadData() {
            disposable = animeRepository.getRecentEpisodes()
                    .fromBgToUi()
                    .subscribeBy(
                            onNext = { showData(it) },
                            onError = { /* TODO Display an error */ }
                    )
        }

        private fun showData(recentEpisodes: List<EpisodeReleaseSummary>) {
            adapter.clear()
            adapter.addAll(0, recentEpisodes)
        }

        private fun showAnimeDetails(anime: AnimeSummary) {
            val intent = AnimeDetailsActivity.getIntent(activity, anime)
            activity.startActivity(intent)
        }
    }
}