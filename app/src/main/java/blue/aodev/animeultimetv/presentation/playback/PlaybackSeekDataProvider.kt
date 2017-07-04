package blue.aodev.animeultimetv.presentation.playback

import android.support.v17.leanback.media.PlaybackGlue
import android.support.v17.leanback.media.PlaybackTransportControlGlue
import android.support.v17.leanback.widget.PlaybackSeekDataProvider

/**
 * Created by ao on 04/07/17.
 */
class PlaybackSeekDataProvider : PlaybackSeekDataProvider() {

    companion object {
        /**
         * Helper function to set a demo seek provider on PlaybackTransportControlGlue based on
         * duration.
         */
        fun setDemoSeekProvider(glue: PlaybackTransportControlGlue<*>) {
            if (glue.isPrepared) {
                glue.setSeekProvider(PlaybackSeekDataProvider())
            } else {
                glue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
                    override fun onPreparedStateChanged(glue: PlaybackGlue) {
                        if (glue.isPrepared) {
                            glue.removePlayerCallback(this)
                            val transportControlGlue = glue as PlaybackTransportControlGlue<*>
                            transportControlGlue.seekProvider = PlaybackSeekDataProvider()
                        }
                    }
                })
            }
        }
    }
}