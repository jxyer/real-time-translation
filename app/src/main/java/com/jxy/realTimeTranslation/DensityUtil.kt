package com.jxy.realTimeTranslation

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize

class DensityUtil {
    companion object {

        fun sizeToDpSize(size: Size, density: Density): DpSize {
            with(density) {
                return size.toDpSize()
            }
        }

    }
}