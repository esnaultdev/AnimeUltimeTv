package blue.aodev.animeultimetv.presentation.playback

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.Anime
import blue.aodev.animeultimetv.domain.PlaybackInfo

class PlaybackActivity : Activity() {

    companion object {
        private val EXTRA_PLAYBACK_INFO = "extraPlaybackInfo"

        fun getIntent(context: Context, anime: Anime, episodeNumber: Int): Intent {
            val episode = anime.episodes[episodeNumber]
            val playbackInfo = PlaybackInfo(
                    anime.title,
                    context.getString(R.string.episode_title, episodeNumber + 1),
                    episode.videoUrl,
                    episode.hdVideoUrl,
                    episode.duration
            )

            val intent = Intent(context, PlaybackActivity::class.java)
            intent.putExtra(EXTRA_PLAYBACK_INFO, playbackInfo)
            return intent
        }
    }

    val playbackInfo: PlaybackInfo by lazy {
        intent.getParcelableExtra<PlaybackInfo>(EXTRA_PLAYBACK_INFO)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)

        fragmentManager.beginTransaction()
                .add(R.id.videoFragment, PlaybackFragment(), PlaybackFragment.TAG)
                .commit()
    }
}