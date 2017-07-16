package blue.aodev.animeultimetv.domain.model

data class Anime(
        val id: Int,
        val title: String,
        val imageUrl: String?,
        val type: AnimeType,
        val synopsis: String,
        val totalCount: Int,
        val rating: Float,
        val episodes: List<Episode>,
        val productionYears: IntRange,
        val studios: List<String>,
        val genres: List<String>,
        val author: String
)
