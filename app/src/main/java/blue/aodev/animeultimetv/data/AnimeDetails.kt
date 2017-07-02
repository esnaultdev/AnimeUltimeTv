package blue.aodev.animeultimetv.data

data class AnimeDetails(
        val synopsis: String,
        val productionYears: IntRange,
        val studios: List<String>,
        val genres: List<String>,
        val author: String
)