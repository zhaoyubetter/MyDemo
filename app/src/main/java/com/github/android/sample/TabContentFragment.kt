package com.github.android.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.better.base.holder.Type1Holder
import com.better.base.holder.Type2Holder
import com.better.base.model.SampleItem
import com.github.android.sample.constraintLayout.ConstaintLayoutDemo1Activity
import com.github.better.recycler.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.find

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

    private fun getData(pid: Int?, datas: MutableList<ExpandNode<SampleItem<Activity>>>): MutableList<ExpandNode<SampleItem<Activity>>>? {
        if (pid == null) {
            return null
        }
        // 模板数据
        val curItems = AnimFunTemplate.getInstance(activity)[pid]
        // ExpandNode
        val curExpandNodes = mutableListOf<ExpandNode<SampleItem<Activity>>>()
        curItems?.forEach { it ->
            val node = ExpandNode(it, 0, true)
            curExpandNodes.add(node)                // 父节点
            // 孩子节点
            getData(it.id, mutableListOf())?.let {
                node.addChildren(it)
            }
        }
        datas.addAll(curExpandNodes)
        return datas
    }

    fun refreshLevel() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val datas = mutableListOf<ExpandNode<SampleItem<Activity>>>()
        // 设置节点level

        val recycler = find<RecyclerView>(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = ExpandAdapter(recycler, datas, object : ExpandHolderFactory<SampleItem<Activity>> {
            override fun getHolder(helper: ExpandRecyclerViewHelper<SampleItem<Activity>>,
                                   parent: ViewGroup, viewType: Int): ExpandViewHolder<SampleItem<Activity>> {
                return when (viewType) {
                    0 -> Type1Holder(helper, LayoutInflater.from(context).inflate(R.layout.type_item_1, parent, false))
                    1 -> Type1Holder(helper, LayoutInflater.from(context).inflate(R.layout.type_item_1, parent, false))
                    2 -> Type2Holder(helper, LayoutInflater.from(context).inflate(R.layout.type_item_2, parent, false))
                    else -> Type1Holder(helper, LayoutInflater.from(context).inflate(R.layout.type_item_1, parent, false))
                }
            }
        })
    }
}