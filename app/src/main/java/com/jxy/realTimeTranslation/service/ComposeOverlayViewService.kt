package com.jxy.realTimeTranslation.service

import android.graphics.PixelFormat
import android.os.Build
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.jxy.realTimeTranslation.bean.WindowSize
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.R)
abstract class ComposeOverlayViewService : ViewReadyService() {

    private val windowManager by lazy {
        overlayContext.getSystemService(WindowManager::class.java)
    }

    private val layoutParams by lazy {
        WindowManager.LayoutParams().apply {
            format = PixelFormat.RGBA_8888
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                @Suppress("DEPRECATION")
                type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            } else {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
    }

    // Build our compose view
    private val composeView by lazy {
        ComposeView(overlayContext)
    }


    override fun onCreate() {
        super.onCreate()
        composeView.setViewTreeLifecycleOwner(this)
        composeView.setViewTreeViewModelStoreOwner(this)
        composeView.setViewTreeSavedStateRegistryOwner(this)

        composeView.setContent { Content() }
        windowManager.addView(composeView, layoutParams)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(composeView)
    }


    private var overlayOffset by mutableStateOf(Offset.Zero)

    @Composable
    internal fun OverlayDraggableContainer(
        modifier: Modifier = Modifier,
        content: @Composable BoxScope.() -> Unit
    ) {
        Box(
            modifier = modifier.pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val newOffset = overlayOffset + dragAmount
                    overlayOffset = newOffset

                    //update the layout params,and then the view
                    layoutParams.apply {
                        x = overlayOffset.x.roundToInt()
                        y = overlayOffset.y.roundToInt()
                    }
                    windowManager.updateViewLayout(composeView, layoutParams)
                }
            },
            content = content
        )
    }

    internal fun updateSize(windowSize: WindowSize) {
        layoutParams.apply {
            this.width = windowSize.width
            this.height = windowSize.height
        }
        windowManager.updateViewLayout(composeView, layoutParams)
    }

    internal fun updateTopLeft(topLeft: Offset){
        layoutParams.apply {
            x = topLeft.x.roundToInt()
            y = topLeft.y.roundToInt()
        }
        windowManager.updateViewLayout(composeView, layoutParams)
    }

    /**
     * 浮动视图
     */
    @Composable
    abstract fun Content()

}