package com.github.android.sample.widget.recyler.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.android.sample.widget.recyler.adapter.SimpleAdapter
import com.github.android.sample.widget.recyler.layoutman.FixedGridLayoutManager



class FixedTwoWayFragment : RecyclerViewFragment() {

    companion object {
        fun newInstance(): FixedTwoWayFragment {
            return FixedTwoWayFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        val manager = FixedGridLayoutManager()
        manager.setTotalColumnCount(3)

        return manager
    }

    override fun getItemDecoration() = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(48, 48, 48, 48)
        }
    }

    override fun getDefaultItemCount() = 5

    override fun getAdapter() = SimpleAdapter()

}