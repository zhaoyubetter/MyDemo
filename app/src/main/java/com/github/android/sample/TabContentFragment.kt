package com.github.android.sample

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.android.sample.constraintLayout.ConstaintLayoutDemo1Activity
import org.jetbrains.anko.find

/**
 * Created by zhaoyu on 2018/3/9.
 */
class TabContentFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tab_content, container, false)
        view.find<View>(R.id.btn_constraintLayout).setOnClickListener {
            startActivity(Intent(activity, ConstaintLayoutDemo1Activity::class.java))
        }
        return view
    }
}