package blue.aodev.animeultimetv.presentation.playback

import android.app.Activity
import android.os.Bundle
import blue.aodev.animeultimetv.R

class PlaybackActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)

        fragmentManager.beginTransaction()
                .add(R.id.videoFragment, PlaybackFragment(), PlaybackFragment.TAG)
                .commit()
    }
}