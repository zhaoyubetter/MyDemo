package com.github.android.sample.widget

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.better.base.holder.Type1Holder
import com.better.base.model.SampleItem
import com.github.android.sample.R
import com.github.better.recycler.*
import org.jetbrains.anko.find

/**
 * Created by zhaoyu on 2018/3/9.
 */
class WidgetContentFragment : Fragment() {
    private lateinit var recycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_basic_content, container, false)
        recycler = view.find(R.id.recycler)
        return view
    }

    private fun getData(pid: Int?, datas: MutableList<ExpandNode<SampleItem<Activity>>>, level: Int = 0):
            MutableList<ExpandNode<SampleItem<Activity>>>? {
        if (pid == null) {
            return null
        }
        var level = level
        // 模板数据
        val curItems = WidgetFunItemTemplate.getInstance(activity!!.applicationContext)[pid]
        // ExpandNode
        val curExpandNodes = mutableListOf<ExpandNode<SampleItem<Activity>>>()
        curItems?.forEach { it ->
            val node = ExpandNode(it, 0, level == 1)
            curExpandNodes.add(node)                // 父节点
            // 孩子节点
            getData(it.id, mutableListOf(), level++)?.let {
                node.addChildren(it)
            }
        }
        datas.addAll(curExpandNodes)
        return datas
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val datas = mutableListOf<ExpandNode<SampleItem<Activity>>>()
        getData(0, datas)

        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = ExpandAdapter(recycler, datas, object : ExpandHolderFactory<SampleItem<Activity>> {
            override fun getHolder(helper: ExpandRecyclerViewHelper<SampleItem<Activity>>,
                                   parent: ViewGroup, viewType: Int): ExpandViewHolder<SampleItem<Activity>> {
                return when (viewType) {
                    0 -> Type1Holder(helper, LayoutInflater.from(context).inflate(R.layout.type_item_1, parent, false))
                    1 -> Type1Holder(helper, LayoutInflater.from(context).inflate(R.layout.type_item_2, parent, false))
                    2 -> Type1Holder(helper, LayoutInflater.from(context).inflate(R.layout.type_item_3, parent, false))
                    else -> Type1Holder(helper, LayoutInflater.from(context).inflate(R.layout.type_item_1, parent, false))
                }
            }
        })
    }
}