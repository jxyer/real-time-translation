package com.jxy.realTimeTranslation.bean

import androidx.compose.ui.geometry.Offset

data class OverlayState(
    var windowSize: WindowSize,
    var alpha: Float,
    var windowTopLeft: Offset
)