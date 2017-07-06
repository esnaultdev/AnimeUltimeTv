package blue.aodev.animeultimetv.domain.mapper

import android.content.Context
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.Anime
import blue.aodev.animeultimetv.domain.model.Video
import blue.aodev.animeultimetv.domain.model.Playlist

class AnimeToPlaylistMapper(val context: Context) {

    fun transform(anime: Anime, index: Int): Playlist {
        val playbackInfos = anime.episodes.map { episode ->
            Video(
                    context.getString(R.string.episode_title, episode.number),
                    episode.videoUrl,
                    episode.hdVideoUrl,
                    episode.duration
            )
        }
        return Playlist(anime.title, index, playbackInfos)
    }

}
