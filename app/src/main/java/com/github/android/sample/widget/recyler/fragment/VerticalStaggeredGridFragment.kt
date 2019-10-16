package com.github.android.sample.widget.recyler.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.android.sample.widget.recyler.adapter.SimpleAdapter

/**
 * 瀑布流
 */
class VerticalStaggeredGridFragment : RecyclerViewFragment() {

    companion object {
        fun newInstance(): VerticalStaggeredGridFragment = VerticalStaggeredGridFragment().apply {
            arguments = Bundle()
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getItemDecoration() = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(48, 48, 48, 48)
        }
    }

    override fun getDefaultItemCount() = 100

    override fun getAdapter() = SimpleAdapter()

}