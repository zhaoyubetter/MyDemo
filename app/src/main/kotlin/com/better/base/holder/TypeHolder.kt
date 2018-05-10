package com.better.base.holder

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.better.base.model.SampleItem
import com.github.android.sample.MainActivity
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
    lateinit var node: ExpandNode<SampleItem<Activity>>
    override fun setData(node: ExpandNode<SampleItem<Activity>>) {
        this.node = node
        itemView.find<TextView>(android.R.id.text1).text = node.data.title
        itemView.find<TextView>(android.R.id.text2).text = node.data.desc
        itemView.find<ImageView>(R.id.image).setImageResource(
                if (node.expand) R.mipmap.arrow_icon_down else R.mipmap.arrow_icon_up)

        itemView.find<ImageView>(R.id.image).visibility = if (node.hasChildren()) View.VISIBLE else View.INVISIBLE
        itemView.find<View>(android.R.id.text2).visibility = if (node.hasChildren()) View.GONE else View.VISIBLE

    }

    override fun getOnExpandItemClickListener(): OnExtendedItemClickListener? {
        return object : OnExtendedItemClickListener {
            override fun onExtendedClick() {
                if (node.hasChildren()) {
                    return
                }
                itemView.context.startActivity(Intent(itemView.context, node.data.clazz).apply {
                    putExtra("item", node.data)
                })

            }

            override fun onFoldClick() {
                if (node.hasChildren()) {
                    return
                }
                itemView.context.startActivity(Intent(itemView.context, node.data.clazz).apply {
                    putExtra("item", node.data)
                })
            }
        }
    }

    override fun getExtendedClickView(): View = itemView
}
