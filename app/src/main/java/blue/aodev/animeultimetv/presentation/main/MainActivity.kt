package blue.aodev.animeultimetv.presentation.main

import android.os.Bundle
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.presentation.LeanbackActivity

class MainActivity : LeanbackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}