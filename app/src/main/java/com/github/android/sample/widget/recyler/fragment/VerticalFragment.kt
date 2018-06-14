package com.github.android.sample.widget.recyler.fragment

import com.github.android.sample.widget.recyler.adapter.SimpleAdapter
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout


class VerticalFragment : RecyclerViewFragment() {

    companion object {
        fun newInstance(): VerticalFragment {
            return VerticalFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun getLayoutManager() = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    override fun getItemDecoration() = DividerItemDecoration(activity, LinearLayout.VERTICAL)

    override fun getDefaultItemCount() = 100

    override fun getAdapter(): SimpleAdapter = SimpleAdapter()

}