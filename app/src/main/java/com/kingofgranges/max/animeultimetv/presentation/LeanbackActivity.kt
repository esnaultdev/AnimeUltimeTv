package com.kingofgranges.max.animeultimetv.presentation

import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.kingofgranges.max.animeultimetv.presentation.search.SearchActivity

/**
 * This parent class contains common methods that run in every activity such as search.
 */
abstract class LeanbackActivity : FragmentActivity() {

    override fun onSearchRequested(): Boolean {
        startActivity(Intent(this, SearchActivity::class.java))
        return true
    }

}