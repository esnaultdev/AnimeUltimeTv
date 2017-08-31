package blue.aodev.animeultimetv.presentation.screen.animedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.presentation.screen.LeanbackActivity

class AnimeDetailsActivity : LeanbackActivity() {

    companion object {
        private val EXTRA_ANIME_SUMMARY = "extraAnimeSummary"

        fun getIntent(context: Context, animeSummary: AnimeSummary): Intent {
            val intent = Intent(context, AnimeDetailsActivity::class.java)
            intent.putExtra(EXTRA_ANIME_SUMMARY, animeSummary)
            return intent
        }
    }

    val animeSummary: AnimeSummary by lazy {
        intent.getParcelableExtra<AnimeSummary>(EXTRA_ANIME_SUMMARY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_details)
    }
}