package blue.aodev.animeultimetv.presentation.search

import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.widget.*
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.AnimeInfo
import blue.aodev.animeultimetv.data.AnimeUltimeRepositoryImpl
import blue.aodev.animeultimetv.domain.AnimeUltimeRepository
import blue.aodev.animeultimetv.presentation.animedetails.AnimeDetailsActivity
import blue.aodev.animeultimetv.presentation.common.AnimeCardPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SearchFragment : android.support.v17.leanback.app.SearchFragment(),
        android.support.v17.leanback.app.SearchFragment.SearchResultProvider {

    private lateinit var rowsAdapter: ArrayObjectAdapter
    private val animeAdapter = ArrayObjectAdapter(AnimeCardPresenter())

    private var query = ""
    private var hasResults = false
    private val repository: AnimeUltimeRepository = AnimeUltimeRepositoryImpl() // TODO Inject
    private var currentSearch: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        setSearchResultProvider(this)
        setOnItemViewClickedListener { _, item, _, _ ->
            if (item is AnimeInfo) { showAnimeDetails(item) } }
    }

    override fun onStart() {
        super.onStart()
        updateVerticalOffset()
    }

    override fun getResultsAdapter(): ObjectAdapter {
        return rowsAdapter
    }

    override fun onQueryTextChange(newQuery: String): Boolean {
        search(newQuery)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        search(query)
        return true
    }

    private fun search(query: String) {
        if (query == this.query) return

        this.query = query

        if (query.length < 2) {
            clearSearchResults()
            return
        }

        currentSearch?.dispose()

        currentSearch = repository.search(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { onSearchResult(it) },
                        onError = { onSearchResult(emptyList()) }
                )
    }

    private fun onSearchResult(results: List<AnimeInfo>) {
        animeAdapter.clear()
        hasResults = results.isNotEmpty()
        val row = if (hasResults) {
            animeAdapter.addAll(0, results)
            ListRow(animeAdapter)
        } else {
            val header = HeaderItem(getString(R.string.search_noResults, query))
            ListRow(header, animeAdapter)
        }
        rowsAdapter.clear()
        rowsAdapter.add(row)
        updateVerticalOffset()
    }

    private fun clearSearchResults() {
        rowsAdapter.clear()
    }

    fun hasResults(): Boolean {
        return hasResults
    }

    fun focusOnSearch() {
        view!!.findViewById(R.id.lb_search_bar).requestFocus()
    }

    private fun updateVerticalOffset() {
        // Override the vertical offset. Not really pretty.
        val offsetResId = if (!hasResults()) {
            R.dimen.searchFragment_rowMarginTop_withHeader
        } else {
            R.dimen.searchFragment_rowMarginTop_withoutHeader
        }

        rowsFragment.verticalGridView.windowAlignmentOffset =
                resources.getDimensionPixelSize(offsetResId)
    }

    private fun showAnimeDetails(anime: AnimeInfo) {
        val intent = Intent(activity, AnimeDetailsActivity::class.java)
        activity.startActivity(intent)
    }
}