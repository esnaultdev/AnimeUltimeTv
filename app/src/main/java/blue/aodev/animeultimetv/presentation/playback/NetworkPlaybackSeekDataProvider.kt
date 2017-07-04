package blue.aodev.animeultimetv.presentation.playback

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.support.v17.leanback.media.PlaybackGlue
import android.support.v17.leanback.media.PlaybackTransportControlGlue
import android.util.LruCache


class NetworkPlaybackSeekDataProvider(duration: Long, interval: Long, videoUrl: String)
    : PlaybackSeekAsyncDataProvider() {

    private val metadataRetriever: MediaMetadataRetriever = MediaMetadataRetriever()

    private val bitmapCache: LruCache<Int, Bitmap>

    init {
        val size = (duration / interval).toInt() + 1
        val pos = LongArray(size)
        for (i in pos.indices) {
            pos[i] = i * duration / pos.size
        }
        seekPositions = pos
        metadataRetriever.setDataSource(videoUrl, emptyMap())

        // Use 1/8th of the available memory for this memory cache.
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        bitmapCache = object : LruCache<Int, Bitmap>(cacheSize) {
            override fun sizeOf(key: Int, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.byteCount / 1024
            }
        }
    }

    override fun doInBackground(task: Any?, index: Int, position: Long): Bitmap? {
        if (isCancelled(task)) {
            return null
        }
        val cachedBitmap = bitmapCache.get(index)
        if (cachedBitmap != null) {
            return cachedBitmap
        } else {
            val bitmap = metadataRetriever.getFrameAtTime(position * 1000)
            bitmapCache.put(index, bitmap)
            return bitmap
        }
    }

    companion object {
        /**
         * Helper function to set a demo seek provider on PlaybackTransportControlGlue based on
         * duration.
         */
        fun setDemoSeekProvider(glue: PlaybackTransportControlGlue<*>, videoUrl: String) {
            if (glue.isPrepared) {
                setDemoSeekProviderPrepared(glue, videoUrl)
            } else {
                glue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
                    override fun onPreparedStateChanged(glue: PlaybackGlue) {
                        if (glue.isPrepared) {
                            glue.removePlayerCallback(this)
                            val transportControlGlue = glue as PlaybackTransportControlGlue<*>
                            setDemoSeekProviderPrepared(transportControlGlue, videoUrl)
                        }
                    }
                })
            }
        }

        private fun setDemoSeekProviderPrepared(glue: PlaybackTransportControlGlue<*>,
                                                videoUrl: String) {
            glue.seekProvider = NetworkPlaybackSeekDataProvider(
                    glue.duration,
                    glue.duration / 50,
                    videoUrl
            )
        }
    }
}