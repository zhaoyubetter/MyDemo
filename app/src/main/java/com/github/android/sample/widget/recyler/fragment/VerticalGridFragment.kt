package com.github.android.sample.widget.recyler.fragment

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.android.sample.widget.recyler.adapter.SimpleAdapter

class VerticalGridFragment : RecyclerViewFragment() {

    companion object {
        fun newInstance(): VerticalGridFragment {
            return VerticalGridFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun getLayoutManager() = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false);
    override fun getItemDecoration() = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(48, 48, 48, 48)
        }
    }
    override fun getDefaultItemCount() = 100
    override fun getAdapter() = SimpleAdapter()

}