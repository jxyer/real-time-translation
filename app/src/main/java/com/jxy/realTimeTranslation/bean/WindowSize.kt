package com.jxy.realTimeTranslation.bean

import android.view.WindowManager
import androidx.compose.ui.geometry.Size
import kotlin.math.roundToInt

data class WindowSize(var width: Int, var height: Int) {
    companion object {
        fun warpWindowSize(): WindowSize {
            return WindowSize(
                width = WindowManager.LayoutParams.WRAP_CONTENT,
                height = WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

        fun fillWindowSize(): WindowSize {
            return WindowSize(
                width = WindowManager.LayoutParams.MATCH_PARENT,
                height = WindowManager.LayoutParams.MATCH_PARENT
            )
        }

        fun sizeToWindowSize(size: Size): WindowSize {
            return WindowSize(size.width.roundToInt(), size.height.roundToInt())
        }
    }

}
