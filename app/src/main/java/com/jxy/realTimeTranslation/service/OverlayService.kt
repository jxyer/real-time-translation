package com.jxy.realTimeTranslation.service

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.jxy.realTimeTranslation.ui.screen.overlay.OverlayScreen
import com.jxy.realTimeTranslation.ui.theme.AppTheme
import com.jxy.realTimeTranslation.viewmodel.OverlayViewModel

@RequiresApi(Build.VERSION_CODES.R)
class OverlayService : ComposeOverlayViewService() {

    private val viewModel: OverlayViewModel by lazy {
        OverlayViewModel()
    }

    @Composable
    override fun Content() = OverlayDraggableContainer {
        val overlayState = remember {
            viewModel.overlayState
        }
        updateSize(overlayState.value.windowSize)
        updateTopLeft(overlayState.value.windowTopLeft)
        AppTheme {
            Surface(
                color = Color.Transparent,
            ) {
                viewModel
                OverlayScreen(viewModel)
            }
        }
    }


}

