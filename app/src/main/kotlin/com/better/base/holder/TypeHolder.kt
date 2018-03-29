package com.better.base.holder

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.better.base.model.SampleItem
import com.github.android.sample.R
import com.github.better.recycler.ExpandNode
import com.github.better.recycler.ExpandRecyclerViewHelper
import com.github.better.recycler.ExpandViewHolder
import org.jetbrains.anko.find

/**
 * Created by zhaoyu on 2018/3/27.
 */
class Type1Holder(helper: ExpandRecyclerViewHelper<SampleItem<Activity>>, itemView: View) :
        ExpandViewHolder<SampleItem<Activity>>(helper, itemView) {

    override fun setData(node: ExpandNode<SampleItem<Activity>>) {
        itemView.find<TextView>(R.id.textView).text = node.data.title
        itemView.find<ImageView>(R.id.image).setImageResource(
                if (node.expand) R.mipmap.arrow_icon_down else R.mipmap.arrow_icon_up)
        itemView.find<ImageView>(R.id.image).visibility = if (node.hasChildren()) View.VISIBLE else View.INVISIBLE
    }

    override fun getExtendedClickView(): View = itemView
}

/**
 * 节点，并设置点击事件
 */
class Type2Holder(helper: ExpandRecyclerViewHelper<SampleItem<Activity>>, itemView: View) :
        ExpandViewHolder<SampleItem<Activity>>(helper, itemView) {
    lateinit var node: ExpandNode<SampleItem<Activity>>
    override fun setData(node: ExpandNode<SampleItem<Activity>>) {
        this.node = node
        itemView.find<TextView>(android.R.id.text1).text = node.data.title
        itemView.find<TextView>(android.R.id.text2).text = node.data.desc
    }

    override fun getOnExpandItemClickListener(): OnExtendedItemClickListener? {
        return object : OnExtendedItemClickListener {
            override fun onExtendedClick() {
                itemView.context.startActivity(Intent(itemView.context, node.data.clazz).apply {
                    putExtra("item", node.data)
                })
            }
            override fun onFoldClick() {
                itemView.context.startActivity(Intent(itemView.context, node.data.clazz).apply {
                    putExtra("item", node.data)
                })
            }
        }
    }

    override fun getExtendedClickView(): View = itemView
}
