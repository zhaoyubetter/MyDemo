package com.github.android.sample

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
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
import com.github.android.sample.anim.AnimFunTemplate
import org.jetbrains.anko.*

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
                startActivity(Intent(activity, it.clazz).apply {
                    putExtra("item", it)
                })
            }
        }
    }

    inner class Adapter(val items: List<SampleItem<Activity>>,
                        val listener: ((SampleItem<Activity>) -> Unit)? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int = items.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = with(parent.context) {
                verticalLayout {
                    padding = dip(8)
                    textView {
                        id = android.R.id.text1
                        textSize = 16f
                        typeface = Typeface.DEFAULT_BOLD
                        topPadding = dip(4)
                    }.lparams()
                    textView {
                        id = android.R.id.text2
                        textSize = 14f
                        topPadding = dip(2)
                        bottomPadding = dip(4)
                    }.lparams()
                }
            }
            return object : RecyclerView.ViewHolder(view){}

//            return ViewHolder(LayoutInflater.from(parent.context).
//                    inflate(android.R.layout.simple_list_item_2, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = items[position]
            holder.itemView.find<TextView>(android.R.id.text1).text = item.title
            holder.itemView.find<TextView>(android.R.id.text2).text = item.desc
            if(listener != null) {
                holder.itemView.setOnClickListener { listener?.invoke(item) }
            }
//            holder.bind(item, listener)
        }

//        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            fun bind(item: SampleItem<Activity>, listener: ((SampleItem<Activity>) -> Unit)?) = with(itemView) {
//                find<TextView>(android.R.id.text1).text = item.title
//                find<TextView>(android.R.id.text2).text = item.desc
//                listener?.let {
//                    setOnClickListener { listener(item) }
//                }
//            }
//        }
    }
}