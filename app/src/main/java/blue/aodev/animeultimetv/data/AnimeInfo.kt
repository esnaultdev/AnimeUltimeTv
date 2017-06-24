package blue.aodev.animeultimetv.data

data class AnimeInfo(
        val id: Int,
        val title: String,
        val imageUrl: String?,
        val type: AnimeInfoType,
        val availableCount: Int,
        val totalCount: Int,
        val rating: Double
)

enum class AnimeInfoType { ANIME, OAV, MOVIE }