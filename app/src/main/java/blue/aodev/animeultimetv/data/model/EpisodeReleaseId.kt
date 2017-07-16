package blue.aodev.animeultimetv.data.model

data class EpisodeReleaseId(
        val animeId: Int,
        val numbers: String // Not an IntRange because the server returns non continuous ranges
)
