package blue.aodev.animeultimetv.presentation.main

import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.HeaderItem
import android.support.v17.leanback.widget.ListRow
import android.support.v17.leanback.widget.ListRowPresenter
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.common.AnimeCardPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainFragment : BrowseFragment() {

    @Inject
    lateinit var animeRepository: AnimeRepository

    private lateinit var categoryRowAdapter: ArrayObjectAdapter
    private lateinit var topAnimesAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.graph.inject(this)

        animeRepository.getTopAnimes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { showTopAnimes(it) }
                )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnSearchClickedListener {
            activity.onSearchRequested()
        }

        categoryRowAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = categoryRowAdapter
    }

    private fun showTopAnimes(topAnimes: List<AnimeSummary>) {
        // TODO Only show the main view after loading everything

        val header = HeaderItem(getString(R.string.main_topAnimes_title))
        val presenter = AnimeCardPresenter()
        topAnimesAdapter = ArrayObjectAdapter(presenter)
        val row = ListRow(header, topAnimesAdapter)

        topAnimesAdapter.addAll(0, topAnimes)

        categoryRowAdapter.clear()
        categoryRowAdapter.add(row)
    }
}