package com.github.android.sample.widget.recyler.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import com.github.android.sample.R
import java.util.*


/**
 * https://github.com/devunwired/recyclerview-playground/blob/master/app/src/main/java/com/example/android/recyclerplayground/adapters/SimpleAdapter.java
 */
open class SimpleAdapter : RecyclerView.Adapter<SimpleAdapter.VerticalItemHolder>() {

    var onItemClickListener: AdapterView.OnItemClickListener? = null
    private var items = mutableListOf<GameItem>()

    companion object {
        fun generateDummyData(count: Int): List<SimpleAdapter.GameItem> {
            val items = ArrayList<SimpleAdapter.GameItem>()
            for (i in 0 until count) {
                items.add(SimpleAdapter.GameItem("Losers", "Winners", i, i + 5))
            }
            return items
        }

        fun generateDummyItem(): GameItem {
            return Random().let {
                GameItem("Upset Home", "Upset Away",
                        it.nextInt(100),
                        it.nextInt(100))
            }
        }
    }

    fun setItemCount(itemCount: Int) {
        items.clear()
        items.addAll(generateDummyData(count = itemCount))
        notifyDataSetChanged()
    }

    fun addItem(position: Int) {
        if (position <= items.size) {   // insert
            items.add(position, generateDummyItem())
            notifyItemInserted(position)
        }
    }

    fun removeItem(position: Int) {
        if (position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalItemHolder {
        return VerticalItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_match_item, parent, false),
                this)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VerticalItemHolder, position: Int) {
        items[position].apply {
            holder.tv_awayScore.text = this.awayScore.toString()
            holder.tv_homeScore.text = this.homeScore.toString()
            holder.tv_awayName.text = this.awayTeam
            holder.tv_homeName.text = this.homeTeam
        }
    }

    private fun onItemHolderClick(holder: VerticalItemHolder) {
        onItemClickListener?.let {
            it.onItemClick(null, holder.itemView, holder.adapterPosition, holder.itemId)
        }
    }

    data class GameItem(val homeTeam: String, val awayTeam: String, val homeScore: Int, val awayScore: Int)

    class VerticalItemHolder(itemView: View,
                             adapter: SimpleAdapter)
        : RecyclerView.ViewHolder(itemView) {

        val tv_homeScore: TextView = itemView.findViewById(R.id.text_score_home) as TextView
        val tv_awayScore: TextView = itemView.findViewById(R.id.text_score_away) as TextView
        val tv_awayName: TextView = itemView.findViewById(R.id.text_team_away) as TextView
        val tv_homeName: TextView = itemView.findViewById(R.id.text_team_home) as TextView

        init {
            itemView.setOnClickListener {
                adapter.onItemHolderClick(this)
            }
        }
    }
}