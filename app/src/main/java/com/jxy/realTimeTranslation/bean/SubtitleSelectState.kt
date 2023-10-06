package com.jxy.realTimeTranslation.bean

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

data class SubtitleSelectState(
    var topLeft: Offset = Offset.Zero,
    var size: Size = Size.Zero,
    var color: Color = Color.Black,
    var fontSize: TextUnit = TextUnit(14f, TextUnitType.Unspecified)
)