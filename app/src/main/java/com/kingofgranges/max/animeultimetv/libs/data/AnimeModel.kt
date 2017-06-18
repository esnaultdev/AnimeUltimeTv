package com.kingofgranges.max.animeultimetv.libs.data

data class AnimeModel(
        val title: String,
        val image: String,
        val synopsis: String,
        val episodes: Array<String>,
        val links: Array<String>
)
