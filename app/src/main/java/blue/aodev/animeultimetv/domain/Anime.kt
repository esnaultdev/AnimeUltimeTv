package blue.aodev.animeultimetv.domain

data class Anime(
        val id: Int,
        val title: String,
        val imageUrl: String?,
        val type: AnimeType,
        val description: String,
        val totalCount: Int,
        val rating: Float,
        val episodes: List<Episode>
)
