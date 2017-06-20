package com.kingofgranges.max.animeultimetv.data

import com.google.gson.annotations.SerializedName
import com.kingofgranges.max.animeultimetv.domain.SearchNetworkEntity

class SearchNetworkModel {
    @SerializedName("id") var id: Long = -1L
    @SerializedName("title") var title: String = ""
    @SerializedName("type") var type: String = ""
    @SerializedName("url") var url: String? = null
    // @SerializedName("number") var number: String  FIXME can be a string or a number?
    @SerializedName("img_url") var imageUrl: String? = null
    @SerializedName("format") var format: String = ""

    fun toEntity(): SearchNetworkEntity {
        val processedImageUrl = imageUrl?.replace("_thindex.", ".")
        return SearchNetworkEntity(id, title, type, url, processedImageUrl, format)
    }
}
