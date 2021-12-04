package com.scorpion.coloringbook.constants

import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import com.scorpion.coloringbook.R
import java.io.File

class Constants {

    companion object {

        const val COUNT_DOWN_INTERVAL = 1000L
        const val FINAL_COUNT_DOWN = 1500L
        const val GRID_SPAN = 2
        var SCREEN_WIDTH = 0

        fun getScreenWidth(display: Display): Int {
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

        fun getScreenHeight(display: Display): Int {
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }
    }

}