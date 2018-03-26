package com.github.better.recycler

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View


/**
 * 参考：
 * https://github.com/liyuzero/extendedRecyclerView/blob/master/extendedRecyclerView/src/main/java/com/yu/bundles/extended/recyclerview/ExtendedRecyclerAdapter.java
 *
 * Created by zhaoyu on 2018/3/26.
 */
class TreeAdapter<E>(val dataList: MutableList<E>, var treeHolderFactory: TreeHolderFactory)
    : RecyclerView.Adapter<TreeHolder<E>>(), TreeRecyclerViewHelper<E> {

    /**
     * // 树的列表展示节点
     */
    var nodeItems: MutableList<TreeNode<E>>? = null
    var items: MutableList<E> = mutableListOf()
    var rootNode: TreeNode<E>? = null
    private val layoutInflater: LayoutInflater? = null

    var isEnableExtended = true

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TreeHolder<E> {
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: TreeHolder<E>?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    ///------------
    override fun updateSrcData(dataList: List<TreeNode<E>>, extendedHolderFactory: TreeHolderFactory) {
        if (extendedHolderFactory != null) {
            this.treeHolderFactory = extendedHolderFactory
        }
        updateSrcData(dataList, extendedHolderFactory)
    }

    override fun recursionDelete(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun recursionDelete(position: Int, layer: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun usuallyDelete(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onExtendedItemClick(position: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertItems(parent: TreeNode<E>, index: Int, items: ArrayList<TreeNode<E>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> getNode(recyclerPos: Int): TreeNode<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getExtendedRecyclerAdapter(): TreeAdapter<E> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

interface TreeRecyclerViewHelper<T> {
    fun updateSrcData(dataList: List<TreeNode<T>>, extendedHolderFactory: TreeHolderFactory? = null)
    fun recursionDelete(position: Int)
    fun recursionDelete(position: Int, layer: Int)
    fun usuallyDelete(position: Int)
    fun onExtendedItemClick(position: Int): Boolean
    fun insertItems(parent: TreeNode<T>, index: Int, items: ArrayList<TreeNode<T>>)
    fun getCurItemCount(): Int
    fun <T> getNode(recyclerPos: Int): TreeNode<T>
    fun getExtendedRecyclerAdapter(): TreeAdapter<T>
}

interface TreeHolderFactory {
    fun <T> getHolder(helper: TreeRecyclerViewHelper<T>, parent: ViewGroup, viewType: Int): TreeHolder<T>
}

abstract class TreeHolder<T>(val helper: TreeRecyclerViewHelper<T>, itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {
        if (helper is TreeAdapter && helper.isEnableExtended) {
            val view = if (getExtendedClickView() == null) itemView else getExtendedClickView()
            view?.setOnClickListener {
                View.OnClickListener {
                    if (getOnExtendedItemClickListener() != null) {
                        val isExtended = helper.onExtendedItemClick(layoutPosition)
                        if (isExtended) {
                            getOnExtendedItemClickListener()!!.onExtendedClick()
                        } else {
                            getOnExtendedItemClickListener()!!.onFoldClick()
                        }
                    }
                }

            }
        }
    }

    abstract fun setData(node: TreeNode<T>)
    protected abstract fun getExtendedClickView(): View?
    fun getOnExtendedItemClickListener(): OnExtendedItemClickListener? {
        return null
    }

    interface OnExtendedItemClickListener {
        fun onExtendedClick()
        fun onFoldClick()
    }
}
