package blue.aodev.animeultimetv.presentation.screen

import android.content.Intent
import android.support.v4.app.FragmentActivity
import blue.aodev.animeultimetv.presentation.screen.search.SearchActivity

/**
 * This parent class contains common methods that run in every activity such as search.
 */
abstract class LeanbackActivity : FragmentActivity() {

    override fun onSearchRequested(): Boolean {
        startActivity(Intent(this, SearchActivity::class.java))
        return true
    }

}