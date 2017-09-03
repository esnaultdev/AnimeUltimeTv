package blue.aodev.animeultimetv.presentation.screen.episodes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.presentation.screen.playback.PlaybackActivity

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodes)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val episodeIndex =
                    data?.getIntExtra(PlaybackActivity.EXTRA_RESULT_EPISODE_INDEX, -1) ?: -1
            if (episodeIndex != -1) {
                setCurrentEpisodeIndex(episodeIndex)
            }
        }
    }

    private fun setCurrentEpisodeIndex(index: Int) {
        val episodesFragment =
                fragmentManager.findFragmentById(R.id.episodesFragment) as? EpisodesFragment

        episodesFragment?.setSelectedPosition(index)
    }
}
