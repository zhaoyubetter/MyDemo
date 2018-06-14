package com.github.android.sample.widget.recyler

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.NumberPicker

class NumberPickerDialog(ctx: Context, theme: Int = 0) : AlertDialog(ctx, theme), DialogInterface.OnClickListener {

    val picker: NumberPicker = NumberPicker(ctx)
    var numberSelect: ((value: Int) -> Unit)? = null

    constructor(ctx: Context, cancelable: Boolean = true, cancelListener: DialogInterface.OnCancelListener) : this(ctx)


    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (which == BUTTON_POSITIVE) {
            numberSelect?.invoke(picker.value)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.cancel), this)
        setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok), this)
        setView(picker)
        super.onCreate(savedInstanceState)
    }

    fun setPickerRange(min: Int = 0, max: Int = 100) {
        picker.minValue = min
        picker.maxValue = max
    }


}