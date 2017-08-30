package blue.aodev.animeultimetv.presentation.screen.playback

import android.app.Activity
import android.net.Uri
import android.os.Handler
import android.support.v17.leanback.media.PlaybackGlue
import android.support.v17.leanback.media.PlaybackTransportControlGlue
import android.support.v17.leanback.widget.Action
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.PlaybackControlsRow
import android.widget.Toast
import blue.aodev.animeultimetv.domain.model.Playlist


class PlaylistMediaPlayerGlue(context: Activity, impl: ExoPlayerAdapter, var playlist: Playlist)
    : PlaybackTransportControlGlue<ExoPlayerAdapter>(context, impl) {

    private val previousAction = PlaybackControlsRow.SkipPreviousAction(context)
    private val nextAction = PlaybackControlsRow.SkipNextAction(context)
    private val hdAction = PlaybackControlsRow.HighQualityAction(context)

    init {
        updateCurrentVideo()
        playWhenReady()
    }

    private fun updateCurrentVideo() {
        val currentVideo = playlist.currentVideo
        title = playlist.title
        subtitle = currentVideo.title
        playerAdapter.setDataSource(Uri.parse(currentVideo.hdVideoUrl))
        NetworkPlaybackSeekDataProvider.setDemoSeekProvider(this, currentVideo.hdVideoUrl)
    }

    override fun onCreatePrimaryActions(adapter: ArrayObjectAdapter) {
        super.onCreatePrimaryActions(adapter)
        adapter.add(previousAction)
        adapter.add(nextAction)
    }

    override fun onCreateSecondaryActions(adapter: ArrayObjectAdapter) {
        super.onCreateSecondaryActions(secondaryActionsAdapter)
        adapter.add(hdAction)
    }

    override fun onActionClicked(action: Action) {
        if (shouldDispatchAction(action)) {
            dispatchAction(action)
            return
        }
        super.onActionClicked(action)
    }

    private fun shouldDispatchAction(action: Action): Boolean {
        return action === hdAction
    }

    private fun dispatchAction(action: Action) {
        Toast.makeText(context, action.toString(), Toast.LENGTH_SHORT).show()
        val multiAction = action as PlaybackControlsRow.MultiAction
        multiAction.nextIndex()
        notifyActionChanged(multiAction)
    }

    private fun notifyActionChanged(action: PlaybackControlsRow.MultiAction) {
        var index = -1
        if (primaryActionsAdapter != null) {
            index = primaryActionsAdapter!!.indexOf(action)
        }
        if (index >= 0) {
            primaryActionsAdapter!!.notifyArrayItemRangeChanged(index, 1)
        } else {
            if (secondaryActionsAdapter != null) {
                index = secondaryActionsAdapter!!.indexOf(action)
                if (index >= 0) {
                    secondaryActionsAdapter!!.notifyArrayItemRangeChanged(index, 1)
                }
            }
        }
    }

    private val primaryActionsAdapter: ArrayObjectAdapter?
        get() = controlsRow?.primaryActionsAdapter as ArrayObjectAdapter?

    private val secondaryActionsAdapter: ArrayObjectAdapter?
        get() = controlsRow?.secondaryActionsAdapter as ArrayObjectAdapter?

    internal var mHandler = Handler()

    fun playWhenReady() {
        if (isPrepared) {
            play()
        } else {
            addPlayerCallback(object : PlayerCallback() {
                override fun onPreparedStateChanged(glue: PlaybackGlue) {
                    if (glue.isPrepared) {
                        glue.removePlayerCallback(this)
                        glue.play()
                    }
                }
            })
        }
    }

    private fun pauseAndPlay(block: PlaylistMediaPlayerGlue.() -> Unit) {
        pause()
        block()
        playWhenReady()
    }

    override fun next() {
        if (playlist.hasNextVideo) {
            pauseAndPlay {
                playlist = playlist.copy(index = playlist.index + 1)
                updateCurrentVideo()
            }
        }
    }

    override fun previous() {
        if (playlist.index > 0) {
            pauseAndPlay {
                playlist = playlist.copy(index = playlist.index - 1)
                updateCurrentVideo()
            }
        }
    }

    override fun onPlayCompleted() {
        super.onPlayCompleted()
        mHandler.post {
            next()
        }
    }
}
