package com.github.android.sample.opengl

import android.content.Context

/**
 * @author zhaoyu1  2020-07-21
 **/
fun readTextFileFromResource(ctx: Context, resId: Int): String {
    return ctx.resources.openRawResource(resId).bufferedReader().readText()
}