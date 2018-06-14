package com.github.android.sample.widget.recyler

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.widget.recyler.layoutman.FlowLayoutManager2
import org.jetbrains.anko.*

class LayoutManager1Activity : ToolbarActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_manager1)
        setTitleFromIntent(intent)
        recyclerView = find(R.id.recyclerView)

        recyclerView.apply {
            // layoutManager = LinearLayoutManager(this@LayoutManager1Activity)
//            layoutManager = FlowLayoutManager1()
            layoutManager = FlowLayoutManager2()
            adapter = Adapter(resources.getStringArray(R.array.language).toList())
        }
    }

    class Adapter(val data: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = with(parent.context) {
                linearLayout {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    padding = dip(8)
                    textView {
                        id = android.R.id.text1
                        textSize = 18f
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT, height = dip(56))

                }
            }
            return object : RecyclerView.ViewHolder(view) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.find<TextView>(android.R.id.text1).text = data[position]
        }

        override fun getItemCount() = data.size
    }
}
