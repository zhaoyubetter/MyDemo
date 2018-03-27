package com.github.better.recycler


/**
 * 结点
 * Created by zhaoyu on 2018/3/26.
 */
data class ExpandNode<T>
/**
 * constructor
 *  @param data <T>
 *  @param level
 *  @param expand
 *  @param parent
 *  @param son
 */
constructor(var data: T, var level: Int = 0, var expand: Boolean = true,
            var parent: ExpandNode<T>? = null, private val son: MutableList<ExpandNode<T>>? = null) {
    // 孩子
    val children = mutableListOf<ExpandNode<T>>()

    init {
        if (son != null) {
            children.addAll(son)
            son?.forEach { it.parent = this }  // 设置父
        }

        val tParent = parent
        if (level == 0 && tParent != null) {
            this.level = tParent.level + 1
        }
    }

    fun addChildren(index: Int = children.size, sons: List<ExpandNode<T>>): ExpandNode<T> {
        this.children.addAll(index, sons)
        sons.forEach { it.parent = this }  // 设置父
        return this
    }

    fun addChildren(sons: List<ExpandNode<T>>): ExpandNode<T> {
        return addChildren(children.size, sons)
    }

    fun hasChildren() = children.size > 0
}
