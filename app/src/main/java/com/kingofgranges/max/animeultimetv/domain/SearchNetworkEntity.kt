package com.kingofgranges.max.animeultimetv.domain

data class SearchNetworkEntity(
        val id: Long,
        val title: String,
        val type: String,
        val url: String?,
        val imageUrl: String?,
        val format: String
)
