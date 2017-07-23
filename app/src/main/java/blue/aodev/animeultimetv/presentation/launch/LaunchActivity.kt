package blue.aodev.animeultimetv.presentation.launch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LaunchActivity: AppCompatActivity() {

    @Inject
    lateinit var animeRepository: AnimeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.graph.inject(this)

        setContentView(R.layout.activity_launch)

        animeRepository.getAnimes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { if (it.isNotEmpty()) showMain() },
                        onError = { /* TODO: Display an error */ }
                )
    }

    private fun showMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}