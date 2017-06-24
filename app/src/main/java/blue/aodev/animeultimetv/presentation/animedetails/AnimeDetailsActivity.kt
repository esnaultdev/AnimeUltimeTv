package blue.aodev.animeultimetv.presentation.animedetails

import android.os.Bundle
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.presentation.LeanbackActivity

class AnimeDetailsActivity : LeanbackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_details)
    }
}