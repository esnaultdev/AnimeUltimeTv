package com.kingofgranges.max.animeultimetv.presentation.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.kingofgranges.max.animeultimetv.R
import com.kingofgranges.max.animeultimetv.presentation.LeanbackActivity

class SearchActivity : LeanbackActivity() {

    private lateinit var fragment: SearchFragment

    /**
     * Called when the activity is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        fragment = fragmentManager.findFragmentById(R.id.search_fragment) as SearchFragment
    }

    override fun onSearchRequested(): Boolean {
        if (fragment.hasResults()) {
            startActivity(Intent(this, SearchActivity::class.java))
        } else {
            fragment.startRecognition()
        }
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // If there are no results found, press the left key to reselect the microphone
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && !fragment.hasResults()) {
            fragment.focusOnSearch()
        }
        return super.onKeyDown(keyCode, event)
    }
}
