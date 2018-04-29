package com.github.widget.wrapper

/**
 * 获取数组最小值的下标]
 * array.foldIndexed(0) { index, acc, i -> return@foldIndexed if (i < array[acc]) index else acc }
 */
fun Array<Int>.minIndex(): Int? {
    if (isEmpty()) return null
    var min = 0
    for (i in 1..lastIndex) {
        if (this[min] > this[i]) min = i
    }
    return min
}

fun IntArray.minIndex(): Int? {
    if (isEmpty()) return null
    var min = 0
    for (i in 1..lastIndex) {
        if (this[min] > this[i]) min = i
    }
    return min
}

