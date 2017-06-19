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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAnimeService()

        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        setSearchResultProvider(this)
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
        if (query.length < 2) return

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
        val titleRes = when (results.size) {
            0 -> R.string.search_noResults
            else -> R.string.search_results
        }

        val processedResults = results
                .sortedBy { it.title }
                .filter { it.type == "Anime" }

        animeAdapter.clear()
        animeAdapter.addAll(0, processedResults)
        val header = HeaderItem(getString(titleRes, query))
        val row = ListRow(header, animeAdapter)
        rowsAdapter.clear()
        rowsAdapter.add(row)
    }

    fun hasResults(): Boolean {
        return rowsAdapter.size() > 0
    }

    fun focusOnSearch() {
        view!!.findViewById(R.id.lb_search_bar).requestFocus()
    }
}