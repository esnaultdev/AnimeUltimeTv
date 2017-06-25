package blue.aodev.animeultimetv.presentation.main

import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.presentation.application.MyApplication
import javax.inject.Inject

class MainFragment : BrowseFragment() {

    @Inject
    lateinit var animeRepository: AnimeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.graph.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnSearchClickedListener {
            activity.onSearchRequested()
        }
    }
}