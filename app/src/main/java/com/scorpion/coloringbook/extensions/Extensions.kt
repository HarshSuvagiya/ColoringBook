package com.scorpion.coloringbook.extensions

import android.view.View
import android.view.animation.AlphaAnimation

class Extensions {

    companion object {
        fun View.onClickThrottled(function: () -> Unit) {
            val buttonClick = AlphaAnimation(1f, 0.6f)
            this.setOnClickListener {
                this.startAnimation(buttonClick)
                function()
            }
        }
    }

}