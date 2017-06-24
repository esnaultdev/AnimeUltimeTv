package com.kingofgranges.max.animeultimetv.presentation.main

import android.os.Bundle
import blue.aodev.animeultimetv.data.AnimeInfo
import blue.aodev.animeultimetv.data.AnimeInfoAdapter
import blue.aodev.animeultimetv.data.HtmlAUService
import com.kingofgranges.max.animeultimetv.R
import com.kingofgranges.max.animeultimetv.presentation.LeanbackActivity
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit



class MainActivity : LeanbackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
                .baseUrl(HttpUrl.parse("http://www.anime-ultime.net/")!!)
                .addConverterFactory(AnimeInfoAdapter.FACTORY)
                .build()

        val htmlAUService = retrofit.create<HtmlAUService>(HtmlAUService::class.java)
        htmlAUService.getAllAnimes().enqueue(object : Callback<List<AnimeInfo>> {
            override fun onResponse(call: Call<List<AnimeInfo>>?, response: Response<List<AnimeInfo>>?) {

            }

            override fun onFailure(call: Call<List<AnimeInfo>>?, t: Throwable?) {

            }
        })
    }
}