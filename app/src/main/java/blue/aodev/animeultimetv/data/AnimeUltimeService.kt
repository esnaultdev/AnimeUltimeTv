package com.kingofgranges.max.animeultimetv.data

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AnimeUltimeService {

    @FormUrlEncoded
    @Headers("X-Requested-With: XMLHttpRequest")
    @POST("MenuSearch.html")
    fun search(@Field("search") query: String) : Call<List<SearchNetworkModel>>

}
