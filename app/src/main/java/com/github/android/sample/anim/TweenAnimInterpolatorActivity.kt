package com.github.android.sample.anim

import android.os.Bundle
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import com.github.android.sample.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * use AnkoComponent
 * 參考：http://blog.csdn.net/harvic880925/article/details/40049763
 */
class TweenAnimInterpolatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyActivityUI().setContentView(this)
    }

    private inner class MyActivityUI : AnkoComponent<TweenAnimInterpolatorActivity> {

        private inline fun getScaleAnim():Animation {
            val anim = ScaleAnimation(0f, 1.0f,0f,1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f)
            anim.duration = 2000
            return anim
        }

        override fun createView(ui: AnkoContext<TweenAnimInterpolatorActivity>) = with(ui) {
            verticalLayout {
                padding = dip(16)
                val view =view {
                    id = android.R.id.text1
                    backgroundColor = resources.getColor(R.color.colorAccent)
                }.lparams(dip(200), dip(300))

                // AccelerateDecelerate
                button {
                    text = "AccelerateDecelerate"
                    onClick {
                        val anim = getScaleAnim()
                        anim.interpolator = AccelerateDecelerateInterpolator()
                        view.startAnimation(anim)
                    }
                }.lparams(wrapContent, wrapContent)

                // AnticipateOvershoot
                button {
                    text = "AnticipateOvershoot"
                    onClick {
                        val anim = getScaleAnim()
                        anim.interpolator = AnticipateOvershootInterpolator()
                        view.startAnimation(anim)
                    }
                }.lparams(wrapContent, wrapContent) {
                    topMargin = dip(2)
                }

                // Anticipate
                button {
                    text = "Anticipate"
                    onClick {
                        val anim = getScaleAnim()
                        anim.interpolator = AnticipateInterpolator()
                        view.startAnimation(anim)
                    }
                }.lparams(wrapContent, wrapContent) {
                    topMargin = dip(2)
                }

                // Cycle
//                button {
//                    text = "Cycle"
//                    onClick {
//                        val anim = getScaleAnim()
//                        anim.interpolator = CycleInterpolator(0.5f)
//                        view.startAnimation(anim)
//                    }
//                }.lparams(wrapContent, wrapContent) {
//                    topMargin = dip(2)
//                }

                // Bounce
                button {
                    text = "Bounce"
                    onClick {
                        val anim = getScaleAnim()
                        anim.interpolator = BounceInterpolator()
                        view.startAnimation(anim)
                    }
                }.lparams(wrapContent, wrapContent) {
                    topMargin = dip(2)
                }

                // OverShot
                button {
                    text = "OverShot"
                    onClick {
                        val anim = getScaleAnim()
                        anim.interpolator = OvershootInterpolator()
                        view.startAnimation(anim)
                    }
                }.lparams(wrapContent, wrapContent) {
                    topMargin = dip(2)
                }
            }
        }
    }
}
