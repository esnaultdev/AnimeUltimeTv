package blue.aodev.animeultimetv.presentation.playback

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v17.leanback.app.PlaybackFragment
import android.support.v17.leanback.app.VideoFragment
import android.support.v17.leanback.app.VideoFragmentGlueHost
import android.support.v17.leanback.media.PlaybackGlue
import android.util.Log
import blue.aodev.animeultimetv.domain.model.Playlist

class PlaybackFragment : VideoFragment() {

    val playlist: Playlist by lazy {
        (activity as PlaybackActivity).playlist
    }

    private lateinit var mediaPlayerGlue: VideoMediaPlayerGlue<ExoPlayerAdapter>
    internal val host = VideoFragmentGlueHost(this)

    internal var onAudioFocusChangeListener: AudioManager.OnAudioFocusChangeListener =
            AudioManager.OnAudioFocusChangeListener { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playerAdapter = ExoPlayerAdapter(activity)
        playerAdapter.audioStreamType = AudioManager.USE_DEFAULT_STREAM_TYPE
        mediaPlayerGlue = VideoMediaPlayerGlue(activity, playerAdapter)
        mediaPlayerGlue.host = host
        val audioManager = activity
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN) != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.w(TAG, "video player cannot obtain audio focus!")
        }

        val currentVideo = playlist.currentVideo
        mediaPlayerGlue.title = playlist.title
        mediaPlayerGlue.subtitle = currentVideo.title
        mediaPlayerGlue.playerAdapter.setDataSource(Uri.parse(currentVideo.hdVideoUrl))
        NetworkPlaybackSeekDataProvider.setDemoSeekProvider(mediaPlayerGlue, currentVideo.hdVideoUrl)
        playWhenReady(mediaPlayerGlue)
        backgroundType = PlaybackFragment.BG_LIGHT
    }

    override fun onPause() {
        mediaPlayerGlue.pause()
        super.onPause()
    }

    companion object {

        val TAG = "PlaybackFragment"

        internal fun playWhenReady(glue: PlaybackGlue) {
            if (glue.isPrepared) {
                glue.play()
            } else {
                glue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
                    override fun onPreparedStateChanged(glue: PlaybackGlue) {
                        if (glue.isPrepared) {
                            glue.removePlayerCallback(this)
                            glue.play()
                        }
                    }
                })
            }
        }
    }
}