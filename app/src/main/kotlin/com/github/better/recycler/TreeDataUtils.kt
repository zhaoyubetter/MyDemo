package com.github.better.recycler

import java.util.*

/**
 * Created by zhaoyu on 2018/3/26.
 */
class TreeDataUtils<T> constructor(val srcDataList: MutableList<TreeNode<T>>) {

    private val transformOriginDataList = mutableListOf<TreeNode<T>>()
    private lateinit var posMapArray: IntArray
    private var curAvailableCount: Int = 0

    init {
        updateSrcData(srcDataList)
    }

    fun updateSrcData(dataList: MutableList<TreeNode<T>>) {
        //更新originDataList
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
        if (dataList == null) {
            return
        }
        for (baseBean in dataList) {
            baseBean.level = layerLevel
            transformOriginDataList.add(baseBean)
            transform(baseBean.children, layerLevel + 1, transformOriginDataList)
        }
    }

    /**
     * 遍历转换后的数据集合，得到折叠后的数据位置和原始数据集合位置的映射数组
     *
     * return 映射数组的实际大小，也是视图上所需展示的未被折叠的item的个数
     *
     * */
    private fun obtainPosMapArray(transformOriginDataList: List<TreeNode<T>>, posMapArray: IntArray): Int {
        var i = 0
        var j = 0
        while (j < transformOriginDataList.size) {
            posMapArray[i++] = j
            val node = transformOriginDataList[j]
            val curLayer = node.level
            //如果折叠
            if (!node.expand) {
                j++
                while (j < transformOriginDataList.size && transformOriginDataList[j].level > curLayer) {
                    j++
                }
            } else {
                j++
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

    /*
    * 获取折叠或展开所需要改变的item的位置范围, startPos, count
    * 改变动作包括：插入，删除，改变
    * */
    private fun getItemRange(position: Int): IntArray {
        val curLevel = transformOriginDataList.get(posMapArray!![position]).layerLevel
        val res = intArrayOf(-1, -1)
        var count = 0
        var i = posMapArray!![position] + 1
        while (i < transformOriginDataList.size && transformOriginDataList.get(i).layerLevel > curLevel) {
            if (res[0] == -1) {
                res[0] = position + 1
            }
            if (!transformOriginDataList.get(i).isExtended) {
                val itemLevel = transformOriginDataList.get(i).layerLevel
                var j: Int
                j = i + 1
                while (j < transformOriginDataList.size && transformOriginDataList.get(j).layerLevel > itemLevel) {
                    j++
                }
                i = j
            } else {
                i++
            }
            count++
        }
        res[1] = count
        return res
    }

    /*-------------------------------------------------------删除操作------------------------------------------------------------*/

    fun getRecursionDeleteNode(position: Int, layer: Int): ExtendedNode {
        var deleteNode = transformOriginDataList.get(posMapArray!![position])
        var parent = deleteNode.parent
        while (parent != null && parent!!.getSons().size() <= 1 && parent!!.layerLevel > layer) {
            deleteNode = parent
            parent = parent!!.parent
        }
        return deleteNode
    }

    /*
    * return 删除节点子树包含的展开的节点数量
    * */
    fun deleteNode(deleteNode: ExtendedNode): Int {
        val deleteCount = getAvailableCount(deleteNode)

        //删除源数据
        val parent = deleteNode.parent
        deleteNode.parent = null
        if (parent != null) {
            parent!!.getSons().remove(deleteNode)
        } else {
            srcDataList!!.remove(deleteNode)
        }

        //删除转换集合内的数据
        val deleteNodeLayerLevel = deleteNode.layerLevel
        val deletePos = transformOriginDataList.indexOf(deleteNode)
        transformOriginDataList.removeAt(deletePos)
        while (deletePos < transformOriginDataList.size && transformOriginDataList.get(deletePos).layerLevel > deleteNodeLayerLevel) {
            transformOriginDataList.removeAt(deletePos)
        }

        curAvailableCount = obtainPosMapArray(transformOriginDataList, posMapArray)

        return deleteCount
    }

    //找出Node在RecyclerView中的位置【注：onBindViewHolder中的position并不总是对应list数据中的位置，所以不可采用，{“Do not treat position as fixed”}】
    fun getNodeRecyclerPos(deleteNode: ExtendedNode): Int {
        val tempIndex = transformOriginDataList.indexOf(deleteNode)
        var deleteNodeIndex = -1
        for (i in 0 until curAvailableCount) {
            if (posMapArray!![i] == tempIndex) {
                deleteNodeIndex = i
                break
            }
        }
        return deleteNodeIndex
    }

    /*-----------------------------------------增加节点-------------------------------------------*/

    //返回需要扩展开的父类节点位置集合
    fun insertItems(parent: ExtendedNode, sonInsertIndex: Int, items: ArrayList<ExtendedNode>?): List<ExtendedNode>? {
        var sonInsertIndex = sonInsertIndex
        if (sonInsertIndex < 0 || items == null || items.size() <= 0) {
            return null
        }

        //设置层级，转换数据
        val transformItems = ArrayList()
        transform(items, parent.layerLevel + 1, transformItems)

        //设置转换数据集合
        val sons = parent.getSons()
        if (sonInsertIndex > sons.size()) {
            sonInsertIndex = sons.size()
        }

        val index: Int
        if (sonInsertIndex < sons.size()) {
            val node = sons.get(sonInsertIndex)
            index = transformOriginDataList.indexOf(node)
            transformOriginDataList.addAll(index, transformItems)
        } else {
            val node = getLastSonNode(parent)
            index = transformOriginDataList.indexOf(node) + 1
            transformOriginDataList.addAll(index, transformItems)
        }
        posMapArray = IntArray(transformOriginDataList.size)
        curAvailableCount = obtainPosMapArray(transformOriginDataList, posMapArray)

        val tempList = ArrayList()
        var node: ExtendedNode? = parent
        while (node != null && !node!!.isExtended) {
            tempList.add(node)
            node = parent.parent
        }
        Collections.reverse(tempList)

        tempList.add(ExtendedNode(index, true))

        //设置原始数据集合
        parent.addSons(sonInsertIndex, items)

        return tempList
    }

    /*---------------------------------------------数据通用操作-----------------------------------------------------*/

    private fun getLastSonNode(node: ExtendedNode): ExtendedNode {
        return if (!node.hasSons()) {
            node
        } else getLastSonNode(node.getSons().get(node.getSons().size() - 1) as ExtendedNode)
    }

    fun getAvailableCount(deleteNode: ExtendedNode?): Int {
        if (deleteNode == null) {
            return 0
        }
        if (!deleteNode!!.isExtended || deleteNode!!.getSons() == null || deleteNode!!.getSons().size() === 0) {
            return 1
        }
        var count = 1
        for (son in deleteNode!!.getSons()) {
            count += getAvailableCount(son as ExtendedNode)
        }
        return count
    }

    fun getExtendedNode(recyclerPosition: Int): ExtendedNode {
        return transformOriginDataList.get(posMapArray!![recyclerPosition])
    }

    fun getCurAvailableCount(): Int {
        return curAvailableCount
    }

    fun getTransformOriginDataList(): List<ExtendedNode> {
        return transformOriginDataList
    }
}