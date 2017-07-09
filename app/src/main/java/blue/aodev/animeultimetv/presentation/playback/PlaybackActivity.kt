package blue.aodev.animeultimetv.presentation.playback

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.mapper.AnimeToPlaylistMapper
import blue.aodev.animeultimetv.domain.model.Anime
import blue.aodev.animeultimetv.domain.model.Playlist

class PlaybackActivity : Activity() {

    companion object {
        private val EXTRA_PLAYLIST = "extraPlaylist"

        fun getIntent(context: Context, anime: Anime, index: Int): Intent {
            val mapper = AnimeToPlaylistMapper(context)
            val playlist = mapper.transform(anime, index)

            val intent = Intent(context, PlaybackActivity::class.java)
            intent.putExtra(EXTRA_PLAYLIST, playlist)
            return intent
        }
    }

    val playlist: Playlist by lazy {
        intent.getParcelableExtra<Playlist>(EXTRA_PLAYLIST)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        fragmentManager.beginTransaction()
                .add(R.id.videoFragment, PlaybackFragment(), PlaybackFragment.TAG)
                .commit()
    }
}