package com.github.android.sample.jetpack.architecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_seek_bar_test.seekBar

class SeekBarTestActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seek_bar_test)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SeekBarFragment()).commit()

        val viewMode = ViewModelProviders.of(this)
                .get(SeekBarFragment.SeekbarViewModel::class.java)

        // 事件
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewMode.liveDataSeek.value = progress
                }
            }
        })

        // 监听
        viewMode.liveDataSeek.observe(this, Observer<Int> {
            seekBar.progress = it
        })
    }
}
