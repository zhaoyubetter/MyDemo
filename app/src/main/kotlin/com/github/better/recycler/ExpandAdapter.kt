package com.github.better.recycler

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup


/**
 * https://github.com/liyuzero/extendedRecyclerView/blob/master/extendedRecyclerView/src/main/java/com/yu/bundles/extended/recyclerview/ExtendedRecyclerAdapter.java
 *
 */
class ExpandAdapter<E>(val recyclerView: RecyclerView, dataList: MutableList<ExpandNode<E>>,
                       var treeHolderFactory: ExpandHolderFactory<E>)
    : RecyclerView.Adapter<ExpandViewHolder<E>>(), ExpandRecyclerViewHelper<E> {

    //    var nodeItems: MutableList<TreeNode<E>>? = null
//    var items: MutableList<E> = mutableListOf()
//    var rootNode: TreeNode<E>? = null
    var isEnableExpand = true
    private val dataUtils: ExpandDataUtils<E> = ExpandDataUtils(dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandViewHolder<E> {
        return treeHolderFactory.getHolder(this, parent, viewType)
    }

    override fun getItemCount() = dataUtils.curAvailableCount

    override fun onBindViewHolder(holder: ExpandViewHolder<E>, position: Int) {
        holder.setData(dataUtils.getExtendedNode(position))
    }

    override fun getItemViewType(position: Int): Int {
        return dataUtils.getExtendedNode(position).level
    }

    /*------------------------------------- 接口实现 -----------------------------------------------*/
    override fun updateSrcData(dataList: List<ExpandNode<E>>, extendedHolderFactory: ExpandHolderFactory<E>?) {
        extendedHolderFactory?.let {
            this.treeHolderFactory = it
        }
        dataUtils.updateSrcData(dataList)
    }

    override fun recursionDelete(position: Int, level: Int) {
        val deleteNode = dataUtils.getRecursionDeleteNode(position, level) ?: return
        val deletePos = dataUtils.getNodeRecyclerPos(deleteNode)
        if (deletePos <= -1) {
            return
        }
        if (deleteNode.parent != null) {
            val parent = deleteNode.parent
            notifyItemChanged(dataUtils.getNodeRecyclerPos(parent!!))
        }
        notifyItemRangeRemoved(deletePos, dataUtils.deleteNode(deleteNode))
    }

    override fun usuallyDelete(position: Int) {
        val deleteNode = dataUtils.getExtendedNode(position)
        val deletePos = dataUtils.getNodeRecyclerPos(deleteNode)
        if (deletePos == -1) {
            return
        }
        // 有parent，则更新parent
        if (deleteNode.parent != null) {
            notifyItemChanged(dataUtils.getNodeRecyclerPos(deleteNode.parent!!))
        }
        notifyItemRangeRemoved(deletePos, dataUtils.deleteNode(deleteNode))
    }

    override fun onExtendedItemClick(position: Int): Boolean {
        val extendNode = dataUtils.getExtendedNode(position)
        // 沒有孩子，直接返回
        if(extendNode.children.size == 0) {
            return extendNode.expand
        }

        val preAvailableCount = dataUtils.curAvailableCount
        // 获取通知更新范围
        val notifyPos = dataUtils.onExtendedItemClick(extendNode, position)
        val curAvailableCount = dataUtils.curAvailableCount
        if (notifyPos[0] != -1) {
            notifyItemChanged(position)
            if (curAvailableCount > preAvailableCount) {
                // expand
                notifyItemRangeInserted(notifyPos[0], notifyPos[1])
                if (notifyPos[0] > (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()) {
                    recyclerView.scrollToPosition(notifyPos[0])
                }
            } else if (curAvailableCount < preAvailableCount) {
                // fold
                notifyItemRangeRemoved(notifyPos[0], notifyPos[1])
            } else {
                // change
                notifyItemRangeChanged(notifyPos[0], notifyPos[1])
            }
        }
        return extendNode.expand
    }

    override fun insertItems(parent: ExpandNode<E>, sonInsertIndex: Int, items: List<ExpandNode<E>>) {
        if (sonInsertIndex < 0) {
            return
        }
        // 所有添加的item个数
        var availableCount = 0
        for (node in items) {
            availableCount += dataUtils.getAvailableCount(node)
        }

        // 插入点
        var insertIndex = 0
        // 插入点，返回的父节点集合
        val tempList = dataUtils.insertItems(parent, sonInsertIndex, items, { it ->
            insertIndex = it
        })
        for (tempNode in tempList) {
            if (!tempNode.expand) {
                onExtendedItemClick(dataUtils.getNodeRecyclerPos(tempNode))
            }
        }
        notifyItemRangeInserted(insertIndex, availableCount)
        notifyItemChanged(dataUtils.getTransformOriginDataList().indexOf(parent))
    }

    override fun getCurItemCount(): Int {
        return dataUtils.curAvailableCount
    }

    override fun getNode(recyclerPos: Int): ExpandNode<E> {
        return dataUtils.getExtendedNode(recyclerPos)
    }

    override fun getExtendedRecyclerAdapter(): ExpandAdapter<E> {
        return this
    }
}


