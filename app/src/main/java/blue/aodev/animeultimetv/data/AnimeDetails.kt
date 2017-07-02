package blue.aodev.animeultimetv.data

data class AnimeDetails(
        val synopsis: String,
        val productionYear: Int,
        val studios: List<String>,
        val genres: List<String>,
        val author: String
)