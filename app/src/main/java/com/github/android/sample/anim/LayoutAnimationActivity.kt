package com.github.android.sample.anim

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_layout_animation.*
import org.jetbrains.anko.*
import java.util.*

class LayoutAnimationActivity : ToolbarActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_animation)

        // 设置 toolbar
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            supportActionBar?.title = it.title
            supportActionBar?.subtitle = it.desc
        }

        recycler = find<RecyclerView>(R.id.recycler)

        // load data
        btn_start.setOnClickListener {
            // 有效
            recycler.apply {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                // 設置無效
                // layoutAnimation = AnimationUtils.loadLayoutAnimation(applicationContext, R.anim.tween_layout)
            }
            recycler.layoutAnimation = AnimationUtils.loadLayoutAnimation(applicationContext, R.anim.tween_layout)
            adapter = Adapter(mutableListOf("java", "Android", "Python", "javascript"))
            recycler.adapter = adapter
        }

        // add item data
        btn_add.setOnClickListener {
            if (adapter != null) {
                adapter + "Better${Random().nextInt(10)}"
            }
        }

        // by code
        btn_bycode.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(baseContext, android.R.anim.slide_in_left)
            recycler.layoutAnimation = LayoutAnimationController(anim).apply {
                delay = 0.3f
                order = LayoutAnimationController.ORDER_REVERSE
            }
            adapter = Adapter(mutableListOf("java", "Android", "Python", "javascript"))
            recycler.adapter = adapter
        }

        /* 报错
        // gridView
        btn_grid.setOnClickListener {
            recycler.apply {
                val anim = AnimationUtils.loadAnimation(baseContext, android.R.anim.slide_in_left)
                layoutAnimation = GridLayoutAnimationController(anim).apply {
                    columnDelay = 0.75f
                    rowDelay = 0.5f
                    direction = GridLayoutAnimationController.DIRECTION_BOTTOM_TO_TOP or GridLayoutAnimationController.DIRECTION_LEFT_TO_RIGHT
                    directionPriority = GridLayoutAnimationController.PRIORITY_NONE
                }

                layoutManager = GridLayoutManager(context,5)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                adapter = Adapter(mutableListOf("java", "Android", "Python", "javascript",
                        "kotlin", "Google", "JetBrains", "Subline", "Html5"))
                recycler.adapter = adapter
            }
        }*/
    }

    class Adapter(val data: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = with(parent.context) {
                verticalLayout {
                    padding = dip(8)
                    textView {
                        id = android.R.id.text1
                        textSize = 16f
                        typeface = Typeface.DEFAULT_BOLD
                        topPadding = dip(4)
                    }.lparams(height = dip(48))
                }
            }
            return object : RecyclerView.ViewHolder(view) {}
        }

        override fun getItemCount() = data.size
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.find<TextView>(android.R.id.text1).text = data[position]
        }

        operator fun plus(item: String) {
            data += item
            notifyItemInserted(data.size - 1)
        }
    }
}
