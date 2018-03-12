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
import android.widget.TextView
import com.better.base.model.SampleItem
import com.github.android.sample.amin.AnimFunTemplate
import org.jetbrains.anko.find

/**
 * Created by zhaoyu on 2018/3/9.
 */
class BasicContentFragment : Fragment() {

    private lateinit var recycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_basic_content, container, false)
        recycler = view.find(R.id.recycler)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = arguments?.getInt("id", 1) ?: 1
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        val items = AnimFunTemplate.getInstance(activity)[id]
        items?.let {
            recycler.adapter = Adapter(it) {
                startActivity(Intent(activity, it.clazz))
            }
        }
    }

    inner class Adapter(val items: List<SampleItem<Activity>>,
                        val listener: (SampleItem<Activity>) -> Unit) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun getItemCount(): Int = items.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).
                    inflate(android.R.layout.simple_list_item_2, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.bind(item, listener)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: SampleItem<Activity>, listener: (SampleItem<Activity>) -> Unit) = with(itemView) {
                find<TextView>(android.R.id.text1).text = item.title
                find<TextView>(android.R.id.text2).text = item.desc
                setOnClickListener { listener(item) }
            }
        }
    }
}