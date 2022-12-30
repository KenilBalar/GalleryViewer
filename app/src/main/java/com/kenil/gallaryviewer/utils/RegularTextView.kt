package com.kenil.gallaryviewer.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.kenil.gallaryviewer.R

class RegularTextView : AppCompatTextView {
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
            val tf = ResourcesCompat.getFont(context, R.font.poppins_regular);
            setTypeface(tf)
    }
}