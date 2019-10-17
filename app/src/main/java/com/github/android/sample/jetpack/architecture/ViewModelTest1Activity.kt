package com.github.android.sample.jetpack.architecture

import android.os.Bundle
import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import android.os.SystemClock.elapsedRealtime
import android.widget.Chronometer
import com.better.base.isNull
import kotlinx.android.synthetic.main.activity_view_model_test1.*


/**
 * ViewModel 例子来自：
 * https://github.com/googlecodelabs/android-lifecycles/blob/master/app/src/main/java/com/example/android/lifecycles/step2/ChronoActivity2.java
 *
 * ViewModel 将视图的数据和逻辑从具有生命周期特性的实体（如 Activity 和 Fragment）中剥离开来。
 * 直到关联的 Activity 或 Fragment 完全销毁时，ViewModel 才会随之消失，也就是说，即使在旋转屏幕导致 Fragment 被重新创建等事件中，
 * 视图数据依旧会被保留。
 * ViewModels 不仅消除了常见的生命周期问题，而且可以帮助构建更为模块化、更方便测试的用户界面。
 */
class ViewModelTest1Activity : ToolbarActivity() {

    // activity 重建时，onCreate 会走
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model_test1)

        // The ViewModelStore provides a new ViewModel or one previously created.
        val chronometerViewModel = ViewModelProviders.of(this).get(ChronometerViewModel::class.java)

        // Get the chronometer reference
        val chronometer = findViewById<Chronometer>(R.id.chronometer)

        chronometerViewModel.startTime.let {
            if (it.isNull()) {
                // If the start date is not defined, it's a new ViewModel so set it.
                val startTime = elapsedRealtime()
                chronometerViewModel.startTime = startTime
                chronometerViewModel.helloWorld = "better"
                chronometer.base = startTime
            } else {
                // Otherwise the ViewModel has been retained, set the chronometer's base to the original
                // starting time.
                chronometer.base = chronometerViewModel.startTime ?: 0
            }
        }

        hello_textview.text = chronometerViewModel.helloWorld
        chronometer.start()

    }

    // ViewModel
    class ChronometerViewModel(var startTime: Long? = null, var helloWorld: String = "") : ViewModel()
}


