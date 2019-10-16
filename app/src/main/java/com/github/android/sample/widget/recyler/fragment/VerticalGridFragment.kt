package com.github.android.sample.widget.recyler.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.android.sample.widget.recyler.adapter.SimpleAdapter

class VerticalGridFragment : RecyclerViewFragment() {

    companion object {
        fun newInstance(): VerticalGridFragment {
            return VerticalGridFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun getLayoutManager() = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false);
    override fun getItemDecoration() = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(48, 48, 48, 48)
        }
    }
    override fun getDefaultItemCount() = 100
    override fun getAdapter() = SimpleAdapter()

}