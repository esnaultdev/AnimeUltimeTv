package com.kingofgranges.max.animeultimetv.data

import com.google.gson.annotations.SerializedName

class SearchNetworkModel {
    @SerializedName("id") var id: Long = -1L
    @SerializedName("title") var title: String = ""
    @SerializedName("type") var type: String = ""
    @SerializedName("url") var url: String? = null
    // @SerializedName("number") var number: String  FIXME can be a string or a number?
    @SerializedName("img_url") var imageUrl: String? = null
    @SerializedName("format") var format: String = ""
}
