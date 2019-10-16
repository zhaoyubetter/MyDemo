package com.github.android.sample.widget.recyler.fragment

import com.github.android.sample.widget.recyler.adapter.SimpleAdapter
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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


    override fun getLayoutManager() = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

    override fun getItemDecoration() = DividerItemDecoration(activity, LinearLayout.VERTICAL)

    override fun getDefaultItemCount() = 100

    override fun getAdapter(): SimpleAdapter = SimpleAdapter()

}