package com.github.android.sample.jetpack.architecture

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_live_data_test1.*
import java.util.*

/**
 * liveData 与 viewModel 的结合使用
 * LiveData 用来监听数据改变，是一种可观察的数据存储器
 * ViewModel 用于链接UI生命周期
 * liveData 与 ViewModel 联合使用
 */
class LiveDataTest1Activity : ToolbarActivity() {

    private lateinit var liveDataTimerModel: LiveDataTimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_test1)

        // The ViewModelStore provides a new ViewModel or one previously created.
        liveDataTimerModel = ViewModelProviders.of(this).get(LiveDataTimerViewModel::class.java)
        subscribe()
    }

    private fun subscribe() {
        // timeObserver 观察者
        val timeObserver = Observer<Long> {
            val newText = "now time is $it"
            timer_textview.text = newText
            Log.d("jetpack-liveData", "Update time: $newText")
        }

        liveDataTimerModel.elapsedTime.observe(this, timeObserver)
//        liveDataTimerModel.elapsedTime.observeForever(timeObserver)  // 永远有效
    }

    // ============== 静态内部类 =========
    class LiveDataTimerViewModel : ViewModel() {
        var elapsedTime = MutableLiveData<Long>()
        var initialTime = SystemClock.elapsedRealtime()

        // 1s 刷新
        init {
            val timer = Timer()
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val newValue = (SystemClock.elapsedRealtime() - initialTime) / 1000
                    // setValue() cannot be called from a background thread so post to main thread.
                    elapsedTime.postValue(newValue)
                }
            }, 1000, 1000)
        }
    }

}
