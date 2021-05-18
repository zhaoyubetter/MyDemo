package com.github.android.sample.jetpack.architecture.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.better.base.d
import com.github.android.sample.R
import kotlinx.android.synthetic.main.fragment_view_model_frag2.*

/**
 * ViewModel:
 * 1. ViewModel 分离数据与UI
 * 2. 避免数据重新创建，fragment 销毁时，数据才会真正销毁；
 * 3. 非常方便 fragment 与 activity 互通数据 （借助 LiveData ）
 * 4.
 */
class ViewModelFrag2 : Fragment() {

    val TAG = "viewModel"
    var viewModel: ScoreViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_model_frag2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // ViewModel associate to fragment
        viewModel = ViewModelProvider(this).get(ScoreViewModel::class.java)
        d(TAG, viewModel.toString())
        // button 点击 + 1
        btn_add.setOnClickListener {
            viewModel?.scoreA = viewModel?.scoreA!!+ 1
            viewModel?.scoreB = viewModel?.scoreB!! + 10
            displayScore()
        }
        displayScore();
    }


    private fun displayScore() {
        tv_1.text = viewModel?.scoreA?.toString()
        tv_2.text = viewModel?.scoreB?.toString()
    }

    class ScoreViewModel : ViewModel() {
        var scoreA = 0;
        var scoreB = 0;
    }
}