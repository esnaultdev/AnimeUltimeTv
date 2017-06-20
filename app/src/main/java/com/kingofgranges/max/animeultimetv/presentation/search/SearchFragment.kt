package com.kingofgranges.max.animeultimetv.presentation.search

import android.os.Bundle
import android.support.v17.leanback.widget.*
import com.kingofgranges.max.animeultimetv.R
import com.kingofgranges.max.animeultimetv.data.AnimeUltimeService
import com.kingofgranges.max.animeultimetv.data.SearchNetworkModel
import com.kingofgranges.max.animeultimetv.presentation.common.CardPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchFragment : android.support.v17.leanback.app.SearchFragment(),
        android.support.v17.leanback.app.SearchFragment.SearchResultProvider {

    private lateinit var rowsAdapter: ArrayObjectAdapter
    private val animeAdapter = ArrayObjectAdapter(CardPresenter())

    private lateinit var auService: AnimeUltimeService
    private var searchCall: Call<List<SearchNetworkModel>>? = null
    private var query = ""
    private var hasResults = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAnimeService()

        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        setSearchResultProvider(this)
    }

    override fun onStart() {
        super.onStart()
        updateVerticalOffset()
    }

    private fun initAnimeService() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://v5.anime-ultime.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        auService = retrofit.create(AnimeUltimeService::class.java)
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
        if (query.length < 2) {
            clearSearchResults()
            return
        }

        if (searchCall != null) searchCall!!.cancel()
        this.query = query

        val newCall = auService.search(query)
        newCall.enqueue(object : Callback<List<SearchNetworkModel>> {
            override fun onResponse(call: Call<List<SearchNetworkModel>>,
                                    response: Response<List<SearchNetworkModel>>) {
                onSearchResult(response.body() ?: emptyList())
            }

            override fun onFailure(call: Call<List<SearchNetworkModel>>, t: Throwable) {
                onSearchResult(emptyList())
            }
        })
    }

    private fun onSearchResult(results: List<SearchNetworkModel>) {
        val processedResults = results
                .sortedBy { it.title }
                .filter { it.type == "Anime" }

        animeAdapter.clear()
        hasResults = results.isNotEmpty()
        val row = if (hasResults) {
            animeAdapter.addAll(0, processedResults)
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
}