package com.github.better.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.LinearLayoutManager


/**
 * Created by zhaoyu1 on 2018/3/27.
 */
/**
 * 自定义Holder
 */
abstract class TreeHolder<T>(val helper: TreeRecyclerViewHelper<T>, itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {
        if (helper is TreeAdapter && helper.isEnableExpand) {
            val view = if (getExtendedClickView() == null) itemView else getExtendedClickView()
            view?.setOnClickListener {
                View.OnClickListener {
                    if (getOnExpandItemClickListener() != null) {
                        val isExtended = helper.onExtendedItemClick(layoutPosition)
                        if (isExtended) {
                            getOnExpandItemClickListener()?.onExtendedClick()
                        } else {
                            getOnExpandItemClickListener()?.onFoldClick()
                        }
                    }
                }
            }
        }
    }

    /**
     * 渲染数据
     */
    abstract fun setData(node: TreeNode<T>)

    /**
     * 获取点击View
     */
    protected abstract fun getExtendedClickView(): View?

    /**
     * 可展开的View点击事件
     */
    fun getOnExpandItemClickListener(): OnExtendedItemClickListener? = null

    interface OnExtendedItemClickListener {
        fun onExtendedClick()
        fun onFoldClick()
    }
}

/**
 * 用来实现TreeAdapter的Helper帮助类
 */
interface TreeRecyclerViewHelper<E> {
    /**
     * 更新数据源
     */
    fun updateSrcData(dataList: List<TreeNode<E>>, extendedHolderFactory: TreeHolderFactory? = null)

    /**
     * 递归删除
     * 当被删除节点的父节点不存在其他孩子节点时，删除父节点，以此向上递归删除
     * @param level: 递归删除至指定层级，level 传 -1
     * */
    fun recursionDelete(position: Int, level: Int = 1)

    /**
     * 刪除节点
     */
    fun usuallyDelete(position: Int)

    /**
     * Item点击后的伸缩事件
     * return 当前点击触发的是扩展事件：true， 触发折叠事件：false
     * */
    fun onExtendedItemClick(position: Int): Boolean

    /**
     * 添加item
     */
    fun insertItems(parent: TreeNode<E>, index: Int, items: List<TreeNode<E>>)

    /**
     * 获取当前item个数
     */
    fun getCurItemCount(): Int

    /**
     * 获取指定位置上的node节点
     */
    fun getNode(recyclerPos: Int): TreeNode<E>

    /**
     * 获取原始adapter
     */
    fun getExtendedRecyclerAdapter(): TreeAdapter<E>
}

/**
 * 生成TreeHolder接口
 */
interface TreeHolderFactory {
    fun <T> getHolder(helper: TreeRecyclerViewHelper<T>, parent: ViewGroup, viewType: Int): TreeHolder<T>
}

/**
 * builder 类
 */
class ExpandRecyclerViewBuilder<T> private constructor(private val recyclerView: RecyclerView) {

    private lateinit var dataList: MutableList<TreeNode<T>>
    private lateinit var extendedHolderFactory: TreeHolderFactory
    private var isEnableExpand = true
    private var isInit: Boolean = false

    companion object {
        fun <T> build(recyclerView: RecyclerView): ExpandRecyclerViewBuilder<T> {
            return ExpandRecyclerViewBuilder(recyclerView)
        }
    }

    init {
        this.recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        isInit = false
    }

    fun init(dataList: MutableList<TreeNode<T>>, extendedHolderFactory: TreeHolderFactory): ExpandRecyclerViewBuilder<T> {
        this.dataList = dataList
        this.extendedHolderFactory = extendedHolderFactory
        isInit = true
        return this
    }

    fun setEnableExpanded(enableExtended: Boolean): ExpandRecyclerViewBuilder<T> {
        isEnableExpand = enableExtended
        return this
    }

    fun complete(): TreeRecyclerViewHelper<T> {
        if (!isInit) {
            throw IllegalArgumentException("must call the method init()")
        }
        val adapter = TreeAdapter(recyclerView, dataList, extendedHolderFactory)
        adapter.isEnableExpand = isEnableExpand
        recyclerView.adapter = adapter
        return adapter
    }
}