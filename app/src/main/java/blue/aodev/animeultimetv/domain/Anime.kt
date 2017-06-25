package blue.aodev.animeultimetv.domain

data class Anime(
        val id: Int,
        val title: String,
        val imageUrl: String?,
        val type: AnimeInfoType,
        val description: String,
        val availableCount: Int,
        val totalCount: Int,
        val rating: Double,
        val episodes: List<Episode>
)
