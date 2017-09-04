package blue.aodev.animeultimetv.presentation.screen.launch

import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.ImageView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.utils.extensions.fromBgToUi
import blue.aodev.animeultimetv.utils.extensions.getDrawableCompat
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.screen.main.MainActivity
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import com.race604.drawable.wave.WaveDrawable


class LaunchActivity: AppCompatActivity() {

    @Inject
    lateinit var animeRepository: AnimeRepository

    @BindView(R.id.launch_logo)
    lateinit var logoView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.graph.inject(this)

        setContentView(R.layout.activity_launch)
        initViews()

        animeRepository.isInitialized()
                .fromBgToUi()
                .subscribeBy(
                        onNext = { if (it) showMain() },
                        onError = { /* TODO: Display an error */ }
                )
    }

    private fun initViews() {
        ButterKnife.bind(this)

        val backgroundDrawable = getDrawableCompat(R.drawable.loading_logo_background)
        val logoDrawable = getDrawableCompat(R.drawable.logo)
        val scaledLogoDrawable = ScaleDrawable(logoDrawable, Gravity.CENTER, 1f, 1f)
                .apply { level = 7000 }
        val fullLogoDrawable = LayerDrawable(arrayOf(backgroundDrawable, scaledLogoDrawable))

        val waveDrawable = WaveDrawable(fullLogoDrawable)
        waveDrawable.isIndeterminate = true

        logoView.setImageDrawable(waveDrawable)
    }

    private fun showMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}