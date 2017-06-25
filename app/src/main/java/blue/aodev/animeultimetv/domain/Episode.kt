package blue.aodev.animeultimetv.domain

data class Episode(
        val title: String,
        val videoUrl: String,
        val imageUrl: String,
        val duration: Int // Duration in seconds
)
