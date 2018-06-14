package com.github.android.sample.widget.recyler.fragment

import com.github.android.sample.widget.recyler.adapter.SimpleAdapter
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout


class HorizontalFragment : RecyclerViewFragment() {

    companion object {
        fun newInstance(): HorizontalFragment {
            return HorizontalFragment().apply {
                arguments = Bundle()
            }
        }
    }


    override fun getLayoutManager() = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

    override fun getItemDecoration() = DividerItemDecoration(activity, LinearLayout.HORIZONTAL)

    override fun getDefaultItemCount() = 40

    override fun getAdapter(): SimpleAdapter = SimpleAdapter()

}