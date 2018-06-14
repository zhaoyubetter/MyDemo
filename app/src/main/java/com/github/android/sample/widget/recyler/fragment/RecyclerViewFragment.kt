package com.github.android.sample.widget.recyler.fragment

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.widget.AdapterView
import android.os.Bundle
import android.view.*
import com.github.android.sample.R
import com.github.android.sample.widget.recyler.adapter.SimpleAdapter
import org.jetbrains.anko.find
import android.view.MenuInflater
import com.github.android.sample.widget.recyler.NumberPickerDialog
import org.jetbrains.anko.support.v4.toast


/**
 * https://github.com/devunwired/recyclerview-playground/blob/master/app/src/main/java/com/example/android/recyclerplayground/fragments/RecyclerFragment.java
 */
abstract class RecyclerViewFragment : Fragment(), AdapterView.OnItemClickListener {

    lateinit var recyclerView: RecyclerView
        private set
    lateinit var simpleAdapter: SimpleAdapter
        private set

    /** Required Overrides for Sample Fragments */

    protected abstract fun getLayoutManager(): RecyclerView.LayoutManager

    protected abstract fun getItemDecoration(): RecyclerView.ItemDecoration
    protected abstract fun getDefaultItemCount(): Int
    protected abstract fun getAdapter(): SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler, container, false).apply {
            recyclerView = find(R.id.section_list)
            recyclerView.layoutManager = getLayoutManager()
            recyclerView.addItemDecoration(getItemDecoration())

            recyclerView.itemAnimator.addDuration = 1000
            recyclerView.itemAnimator.changeDuration = 1000
            recyclerView.itemAnimator.moveDuration = 1000
            recyclerView.itemAnimator.removeDuration = 1000

            simpleAdapter = getAdapter()
            simpleAdapter.itemCount = getDefaultItemCount()
            simpleAdapter.onItemClickListener = this@RecyclerViewFragment
            recyclerView.adapter = simpleAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.grid_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && activity != null) {
            activity?.let {
                val dialog = NumberPickerDialog(it).apply {
                    setTitle("" + item.title)
                }
                when (item.itemId) {
                    R.id.action_add -> {
                        dialog.apply {
                            activity?.setTitle(item.title)
                            setTitle(item.title)
                            setPickerRange(0, simpleAdapter.itemCount)
                            numberSelect = {
                                simpleAdapter.addItem(it)
                            }
                        }.show()
                        return true
                    }

                    R.id.action_remove -> {
                        dialog.apply {
                            setPickerRange(0, simpleAdapter.itemCount - 1)
                            numberSelect = {
                                simpleAdapter.removeItem(it)
                            }
                        }.show()
                        return true
                    }

                    R.id.action_empty -> {
                        simpleAdapter.itemCount = 0
                        return true
                    }

                    R.id.action_small -> {
                        simpleAdapter.itemCount = 5
                        return true
                    }

                    R.id.action_medium -> {
                        simpleAdapter.itemCount = 25
                        return true
                    }

                    R.id.action_scroll_zero -> {
                        recyclerView.scrollToPosition(0)
                        return true
                    }
                    R.id.action_smooth_zero -> {
                        recyclerView.smoothScrollToPosition(0)
                        return true
                    }
                    else -> {
                        return super.onOptionsItemSelected(item)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        toast("Clicked: " + position + ", index " + recyclerView.indexOfChild(view))
    }
}