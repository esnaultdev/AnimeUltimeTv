package blue.aodev.animeultimetv.presentation.episodes

import android.os.Bundle
import android.support.v17.leanback.app.VerticalGridFragment
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.FocusHighlight
import android.support.v17.leanback.widget.OnItemViewClickedListener
import android.support.v17.leanback.widget.Presenter
import android.support.v17.leanback.widget.Row
import android.support.v17.leanback.widget.RowPresenter
import android.support.v17.leanback.widget.VerticalGridPresenter
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.domain.model.Anime
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.model.Episode
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.common.EpisodeCardPresenter
import blue.aodev.animeultimetv.presentation.playback.PlaybackActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EpisodesFragment : VerticalGridFragment() {

    companion object {
        private val COLUMNS = 4
        private val ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_MEDIUM
    }

    @Inject
    lateinit var animeRepository: AnimeRepository

    private lateinit var episodesAdapter: ArrayObjectAdapter
    private var anime: Anime? = null

    val animeSummary: AnimeSummary by lazy {
        (activity as EpisodesActivity).animeSummary
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.graph.inject(this)

        title = animeSummary.title
        setupRowAdapter()

        animeRepository.getAnime(animeSummary.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            this.anime = it
                            updateEpisodes(it.episodes)
                        }
                )
    }

    private fun setupRowAdapter() {
        val gridPresenter = VerticalGridPresenter(ZOOM_FACTOR, false)
        gridPresenter.numberOfColumns = COLUMNS
        setGridPresenter(gridPresenter)

        val cardPresenterSelector = EpisodeCardPresenter()
        episodesAdapter = ArrayObjectAdapter(cardPresenterSelector)
        adapter = episodesAdapter

        onItemViewClickedListener = ItemViewClickedListener()
    }

    private fun updateEpisodes(episodes: List<Episode>) {
        episodesAdapter.clear()
        episodesAdapter.addAll(0, episodes)
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(itemViewHolder: Presenter.ViewHolder, item: Any,
                                   rowViewHolder: RowPresenter.ViewHolder?, row: Row?) {
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