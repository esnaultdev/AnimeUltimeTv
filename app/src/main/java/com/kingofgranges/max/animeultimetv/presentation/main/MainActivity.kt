package com.kingofgranges.max.animeultimetv.presentation.main

import android.os.Bundle
import com.kingofgranges.max.animeultimetv.R
import com.kingofgranges.max.animeultimetv.presentation.LeanbackActivity

class MainActivity : LeanbackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}