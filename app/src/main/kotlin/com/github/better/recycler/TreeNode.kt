package com.github.better.recycler


/**
 * 结点
 * Created by zhaoyu on 2018/3/26.
 */
data class TreeNode<T>
/**
 * constructor
 *  @param data <T>
 *  @param level
 *  @param expand
 *  @param parent
 *  @param son
 */
constructor(var data: T, var level: Int = 0, var expand: Boolean = true,
            var parent: TreeNode<T>? = null, private val son: MutableList<TreeNode<T>>? = null) {
    // 孩子
    val children = mutableListOf<TreeNode<T>>()

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

    fun addChildren(index: Int = children.size, sons: List<TreeNode<T>>): TreeNode<T> {
        this.children.addAll(index, sons)
        sons.forEach { it.parent = this }  // 设置父
        return this
    }

    fun hasChildren() = children.size > 0
}
