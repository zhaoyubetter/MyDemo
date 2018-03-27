package com.github.better.recycler

import java.util.*

/**
 * TreeAdapter 数据处理类
 */
internal class TreeDataUtils<T>

/**
 * @param srcDataList 原始数据
 */
constructor(val srcDataList: MutableList<TreeNode<T>>) {

    /** 处理过的数据，所有的item集合 */
    private val transformOriginDataList = mutableListOf<TreeNode<T>>()
    /** 折叠后的数据位置 和 原始数据集合位置的映射数组, posMapArray[0] = 0, posMapArray[1] = 5(父) 这种 */
    private lateinit var posMapArray: IntArray
    /** 当前可用的item个数 */
    var curAvailableCount: Int = 0
        private set(value) {
            field = value
        }

    init {
        updateSrcData(srcDataList)
    }

    fun updateSrcData(dataList: List<TreeNode<T>>) {
        transform(dataList, 0, transformOriginDataList)
        posMapArray = IntArray(transformOriginDataList.size)
        curAvailableCount = obtainPosMapArray(transformOriginDataList, posMapArray)
    }

    /*-----------------------------树形结构转换为一维数组形式，方便关联Adapter视图-----------------------------------*/

    /**
     * 转换数据
     *
     * @param arrays, 数据数组
     * @param layerLevel, 该数组所处的树内层次
     *
     * */
    private fun transform(dataList: List<TreeNode<T>>, layerLevel: Int, transformOriginDataList: MutableList<TreeNode<T>>) {
        for (baseBean in dataList) {
            baseBean.level = layerLevel
            transformOriginDataList.add(baseBean)
            transform(baseBean.children, layerLevel + 1, transformOriginDataList)
        }
    }

    /**
     * 遍历转换后的数据集合，得到折叠后的数据位置和原始数据集合位置的映射数组
     * return 映射数组的实际大小，也是视图上所需展示的【未被折叠的item的个数】
     **/
    private fun obtainPosMapArray(transformOriginDataList: List<TreeNode<T>>, posMapArray: IntArray): Int {
        var i = 0
        var j = 0
        while (j < transformOriginDataList.size) {
            posMapArray[i++] = j     // posMapArray[0] = 0, posMapArray[1] = 5 这种
            val curNode = transformOriginDataList[j]
            val curLevel = curNode.level
            j++
            // 如果折叠
            if (!curNode.expand) {
                while (j < transformOriginDataList.size && transformOriginDataList[j].level > curLevel) {
                    j++  // 后续的level比curNode的大，即：后续的node为curNode的孩子，故j++
                }
            }
        }

        return i
    }

    /*---------------------------------------------点击Item后的数据转换------------------------------------------------------*/

    /**
     * Item点击后的伸缩事件
     *
     * return 当前点击触发的是扩展事件：true， 触发折叠事件：false
     *
     * */
    fun onExtendedItemClick(extendNode: TreeNode<T>, position: Int): IntArray {
        extendNode.expand = !extendNode.expand
        val notifyPos = getItemRange(position)
        curAvailableCount = obtainPosMapArray(transformOriginDataList, posMapArray)
        return notifyPos
    }

    /**
     * 获取折叠或展开所需要改变的item的位置范围, startPos, count
     * 改变动作包括：插入，删除，改变
     * */
    private fun getItemRange(position: Int): IntArray {
        val curLevel = transformOriginDataList[posMapArray[position]].level
        val res = intArrayOf(-1, -1)
        var count = 0
        var i = posMapArray[position] + 1   // 当前点击的下一个位置开始
        while (i < transformOriginDataList.size && transformOriginDataList[i].level > curLevel) {
            if (res[0] == -1) {
                res[0] = position + 1
            }

            if (!transformOriginDataList.get(i).expand) {   // 如果折叠，则i跳跃时增加
                val itemLevel = transformOriginDataList.get(i).level
                var j = i + 1
                while (j < transformOriginDataList.size && transformOriginDataList.get(j).level > itemLevel) {
                    j++
                }
                i = j   // 跳跃时增加
            } else {
                i++
            }
            count++
        }
        res[1] = count
        return res
    }

    /*-------------------------------------------------------删除操作------------------------------------------------------------*/

    /**
     * 获取递归删除的节点
     */
    fun getRecursionDeleteNode(position: Int, level: Int): TreeNode<T>? {
        if (position > posMapArray.size) {
            return null
        }

        var deleteNode = transformOriginDataList[posMapArray[position]]  // 原始节点
        var parent = deleteNode.parent
        while (parent != null && parent.children.size <= 1 && parent.level > level) {
            deleteNode = parent
            parent = parent.parent
        }
        return deleteNode
    }

    /**
     * return 删除节点子树包含的展开的节点数量
     **/
    fun deleteNode(deleteNode: TreeNode<T>): Int {
        val deleteCount = getAvailableCount(deleteNode)

        //1.设置 deletedNode parent 为 null
        val parent = deleteNode.parent
        deleteNode.parent = null
        // 2. parent不为null时，从parent中删除，否则从原始数据中移除
        if (parent != null) {
            parent.children.remove(deleteNode)
        } else {
            srcDataList.remove(deleteNode)
        }

        // 3. 删除转换集合内的数据
        val deleteNodeLayerLevel = deleteNode.level
        val deletePos = transformOriginDataList.indexOf(deleteNode)
        transformOriginDataList.removeAt(deletePos)
        while (deletePos < transformOriginDataList.size && transformOriginDataList[deletePos].level > deleteNodeLayerLevel) {
            transformOriginDataList.removeAt(deletePos)
        }

        // 4.重新变更数据
        curAvailableCount = obtainPosMapArray(transformOriginDataList, posMapArray)
        return deleteCount
    }

    /**
     * 找出Node在RecyclerView中的位置【注：onBindViewHolder中的position并不总是对应list数据中的位置，
     * 所以不可采用，{“Do not treat position as fixed”}】
     */
    fun getNodeRecyclerPos(deleteNode: TreeNode<T>): Int {
        val tempIndex = transformOriginDataList.indexOf(deleteNode)
        var deleteNodeIndex = -1

        /*for (i in 0 until curAvailableCount) {
            if (posMapArray[i] == tempIndex) {
                deleteNodeIndex = i
                break
            }
        }*/
        // 可使用2分法查找,优化
        return Arrays.binarySearch(posMapArray, tempIndex)
    }

    /*-----------------------------------------增加节点-------------------------------------------*/

    /**
     * 添加节点，返回需要扩展开的父类节点位置集合
     */
    fun insertItems(parent: TreeNode<T>, sonInsertIndex: Int, items: List<TreeNode<T>>, closure: (index: Int) -> Unit): List<TreeNode<T>> {
        var sonInsertIndex = sonInsertIndex
        // 设置层级，并转换数据
        val transformItems = mutableListOf<TreeNode<T>>()
        transform(items, parent.level + 1, transformItems)

        val sons = parent.children
        if (sonInsertIndex > sons.size) {
            sonInsertIndex = sons.size
        }

        val index: Int
        if (sonInsertIndex < sons.size) {    // 中间位置
            val node = sons[sonInsertIndex]
            index = transformOriginDataList.indexOf(node)
            transformOriginDataList.addAll(index, transformItems)
        } else {                            // 最后
            val node = getLastSonNode(parent)
            index = transformOriginDataList.indexOf(node) + 1
            transformOriginDataList.addAll(index, transformItems)
        }

        // 更新 posMapArray 与 curAvailableCount
        posMapArray = IntArray(transformOriginDataList.size)
        curAvailableCount = obtainPosMapArray(transformOriginDataList, posMapArray)

        // 返回扩展开的父类节点位置集合
        val tempList = mutableListOf<TreeNode<T>>()
        var node: TreeNode<T>? = parent
        while (node != null && !node.expand) { // 向上遍历获取父，父收缩时，添加父
            tempList.add(node)
            node = parent.parent
        }
        tempList.reverse()      // 3,2,1 -> 1,2,3
        closure.invoke(index)   // 闭包返回transformOriginDataList的插入index位置
        //设置原始数据集合
        parent.addChildren(sonInsertIndex, items)

        return tempList
    }

    /*---------------------------------------------数据通用操作-----------------------------------------------------*/

    private fun getLastSonNode(node: TreeNode<T>): TreeNode<T> {
        return if (!node.hasChildren()) {
            node
        } else getLastSonNode(node.children.get(node.children.size - 1))
    }

    fun getAvailableCount(node: TreeNode<T>): Int {
        // 没有展开，或者没有孩子 return 1
        if (!node.expand || node.children == null || node.children.size == 0) {
            return 1
        }
        var count = 1
        for (son in node.children) {
            count += getAvailableCount(son)
        }
        return count
    }

    fun getExtendedNode(recyclerPosition: Int): TreeNode<T> {
        return transformOriginDataList[posMapArray[recyclerPosition]]
    }

    fun getTransformOriginDataList(): List<TreeNode<T>> {
        return transformOriginDataList
    }
}

