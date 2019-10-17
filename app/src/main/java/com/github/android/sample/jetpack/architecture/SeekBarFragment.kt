package com.github.android.sample.jetpack.architecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.github.android.sample.R
import kotlinx.android.synthetic.main.fragment_seek_bar_test.*

/**
 * @author zhaoyu1  2019-10-16
 **/
class SeekBarFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seek_bar_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewMode = ViewModelProviders.of(activity!!).get(SeekbarViewModel::class.java)

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

        // liveData 观察者
        viewMode.liveDataSeek.observe(this, Observer<Int> {
            seekBar.progress = it
        })
    }

    class SeekbarViewModel : ViewModel() {
        var liveDataSeek = MutableLiveData<Int>()
    }
}