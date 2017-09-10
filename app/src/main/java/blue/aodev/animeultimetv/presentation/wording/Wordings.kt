package blue.aodev.animeultimetv.presentation.wording

import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.AnimeType

val AnimeType.resId: Int
    get() = when (this) {
        AnimeType.ANIME -> R.string.anime_type_anime
        AnimeType.OAV -> R.string.anime_type_oav
        AnimeType.MOVIE -> R.string.anime_type_movie
    }
