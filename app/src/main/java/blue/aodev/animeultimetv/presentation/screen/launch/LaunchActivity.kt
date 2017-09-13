package blue.aodev.animeultimetv.presentation.screen.launch

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.support.graphics.drawable.ArgbEvaluator
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.AnimeRepository
import blue.aodev.animeultimetv.utils.extensions.fromBgToUi
import blue.aodev.animeultimetv.utils.extensions.getDrawableCompat
import blue.aodev.animeultimetv.presentation.application.MyApplication
import blue.aodev.animeultimetv.presentation.screen.main.MainActivity
import blue.aodev.animeultimetv.utils.SimpleAnimatorListener
import blue.aodev.animeultimetv.utils.extensions.getColorCompat
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

    private lateinit var backgroundDrawable: ShapeDrawable
    private val defaultColor: Int by lazy { getColorCompat(R.color.colorPrimary) }
    private val errorColor: Int by lazy { getColorCompat(R.color.colorError) }

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
                .map { if (it) throw IllegalStateException() else it }
                .fromBgToUi()
                .subscribeBy(
                        onNext = { if (it) showMainAfterAnimation() },
                        onError = { showError() }
                )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { if (!it.isDisposed) it.dispose() }
    }

    //endregion

    private fun initViews() {
        ButterKnife.bind(this)

        backgroundDrawable = ShapeDrawable(OvalShape()).apply { paint.color = defaultColor }
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

    private fun showError() {
        waveAnimator.repeatCount = 0

        val argbEvaluator = ArgbEvaluator()
        val animator = ValueAnimator.ofFloat(0f, 1f)
                .apply {
                    interpolator = AccelerateDecelerateInterpolator()
                    repeatCount = 0
                    duration = 1000L

                    addUpdateListener { animator ->
                        val fraction = animator.animatedFraction
                        val color = argbEvaluator.evaluate(fraction, defaultColor, errorColor)
                        backgroundDrawable.paint.color = color as Int
                    }
                }

        animator.start()
    }
}