package com.jxy.realTimeTranslation.ui.screen.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import com.jxy.realTimeTranslation.bean.WindowSize
import com.jxy.realTimeTranslation.ui.widget.SubtitleSelect
import com.jxy.realTimeTranslation.viewmodel.OverlayViewModel

@Composable
fun Select(
    uiState: MutableState<OverlayViewModel.UiState>,
    viewModel: OverlayViewModel
) {
    val selectUiState = remember {
        viewModel.selectUiState
    }

    when (selectUiState.value) {
        OverlayViewModel.SelectUiState.SELECTING -> {
            viewModel.overlayState.value =
                viewModel.overlayState.value.copy(windowSize = WindowSize.fillWindowSize())
            SubtitleSelect(selectUiState, viewModel)
        }
        OverlayViewModel.SelectUiState.SELECTED -> {
            uiState.value = OverlayViewModel.UiState.PLAY
        }
    }
}
