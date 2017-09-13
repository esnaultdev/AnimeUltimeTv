package blue.aodev.animeultimetv.presentation.screen.launch

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.utils.extensions.fromBgToUi
import blue.aodev.animeultimetv.utils.extensions.getDrawableCompat
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.screen.main.MainActivity
import blue.aodev.animeultimetv.utils.SimpleAnimatorListener
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import com.race604.drawable.wave.WaveDrawable
import io.reactivex.disposables.Disposable


class LaunchActivity: AppCompatActivity() {

    @Inject
    lateinit var animeRepository: AnimeRepository

    @BindView(R.id.launch_logo)
    lateinit var logoView: ImageView

    private var disposable: Disposable? = null

    private lateinit var waveDrawable: WaveDrawable
    private val waveAnimator: ValueAnimator =
            ValueAnimator.ofFloat(0f, 1f)
                    .apply {
                        interpolator = LinearInterpolator()
                        repeatMode = ValueAnimator.RESTART
                        repeatCount = ValueAnimator.INFINITE
                        duration = 3000L
                    }

    //region Lifecyle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.graph.inject(this)

        setContentView(R.layout.activity_launch)
        initViews()

        disposable = animeRepository.isInitialized()
                .fromBgToUi()
                .subscribeBy(
                        onNext = { if (it) showMainAfterAnimation() },
                        onError = { /* TODO: Display an error */ }
                )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { if (!it.isDisposed) it.dispose() }
    }

    //endregion

    private fun initViews() {
        ButterKnife.bind(this)

        val backgroundDrawable = getDrawableCompat(R.drawable.loading_logo_background)
        val logoDrawable = getDrawableCompat(R.drawable.logo)
        val scaledLogoDrawable = ScaleDrawable(logoDrawable, Gravity.CENTER, 1f, 1f)
                .apply { level = 7000 }
        val fullLogoDrawable = LayerDrawable(arrayOf(backgroundDrawable, scaledLogoDrawable))

        waveDrawable = WaveDrawable(fullLogoDrawable)
        waveDrawable.setIndeterminateAnimator(waveAnimator)
        waveDrawable.isIndeterminate = true

        logoView.setImageDrawable(waveDrawable)
    }

    private fun showMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showMainAfterAnimation() {
        waveAnimator.addListener(object : SimpleAnimatorListener {
            override fun onAnimationRepeat(p0: Animator) {
                showMain()
                p0.cancel()
            }

            override fun onAnimationCancel(p0: Animator) { p0.removeListener(this) }
        })
    }
}