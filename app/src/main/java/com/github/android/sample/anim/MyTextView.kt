package com.github.android.sample.anim

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by zhaoyu1 on 2018/3/21.
 */
class MyTextView(ctx: Context, attr: AttributeSet?) :
        TextView(ctx, attr) {
    fun setCharText(char: Char?) {
        text = char?.toString()
    }
}