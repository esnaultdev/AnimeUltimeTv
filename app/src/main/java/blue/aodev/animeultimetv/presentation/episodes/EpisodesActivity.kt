package blue.aodev.animeultimetv.presentation.episodes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.AnimeSummary

class EpisodesActivity : Activity() {

    companion object {
        private val EXTRA_ANIME_SUMMARY = "extraAnimeSummary"

        fun getIntent(context: Context, animeSummary: AnimeSummary): Intent {
            val intent = Intent(context, EpisodesActivity::class.java)
            intent.putExtra(EXTRA_ANIME_SUMMARY, animeSummary)
            return intent
        }
    }

    val animeSummary: AnimeSummary by lazy {
        intent.getParcelableExtra<AnimeSummary>(EXTRA_ANIME_SUMMARY)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodes)
    }
}
