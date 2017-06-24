package blue.aodev.animeultimetv.presentation.main

import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment

class MainFragment : BrowseFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnSearchClickedListener {
            activity.onSearchRequested()
        }
    }
}