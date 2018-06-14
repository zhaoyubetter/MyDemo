package com.github.android.sample.widget.recyler.adapter

import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout
import android.widget.TextView
import com.github.android.sample.R


class GameItemView(ctx: Context, attrs: AttributeSet? = null) : GridLayout(ctx, attrs) {

    lateinit var tv_homeScore: TextView
    lateinit var tv_awayScore: TextView

    override fun onFinishInflate() {
        super.onFinishInflate()

        tv_homeScore = findViewById(R.id.text_score_home)
        tv_awayScore = findViewById(R.id.text_score_away)
    }

    override fun toString(): String {
        return "${tv_awayScore.text} v ${tv_homeScore.text} : $left , $top : $measuredWidth x $measuredHeight"
    }
}