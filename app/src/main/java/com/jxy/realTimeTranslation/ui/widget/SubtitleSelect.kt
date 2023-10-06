package com.jxy.realTimeTranslation.ui.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jxy.realTimeTranslation.DensityUtil
import com.jxy.realTimeTranslation.bean.SubtitleSelectState
import com.jxy.realTimeTranslation.viewmodel.OverlayViewModel
import kotlin.math.roundToInt

@Composable
fun SubtitleSelect(
    selectUiState: MutableState<OverlayViewModel.SelectUiState>,
    viewModel: OverlayViewModel
) {
    val state = remember {
        viewModel.subtitleSelectState
    }
    val uiState = remember {
        viewModel.subtitleUIState
    }
    //是否可以创建
    var canCreate by remember {
        mutableStateOf(true)
    }

    when (uiState.value) {
        OverlayViewModel.SubtitleUIState.INIT -> {
            state.value = SubtitleSelectState()
            canCreate = true
            uiState.value = OverlayViewModel.SubtitleUIState.SELECTING
        }
        OverlayViewModel.SubtitleUIState.SELECTING -> {

        }
        OverlayViewModel.SubtitleUIState.STYLE_UPDATING -> {
            canCreate = false
        }
        OverlayViewModel.SubtitleUIState.SELECTED -> {
            selectUiState.value = OverlayViewModel.SelectUiState.SELECTED
            uiState.value = OverlayViewModel.SubtitleUIState.INIT
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.3f))
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onPress = { offset ->
                    if (canCreate) {
                        state.value = state.value.copy(topLeft = offset)
                    }
                })
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (canCreate) {
                            state.value = state.value.copy(
                                size = Size(
                                    state.value.size.width + dragAmount.x,
                                    state.value.size.height + dragAmount.y
                                )
                            )
                        }
                    },
                    onDragEnd = {
                        uiState.value = OverlayViewModel.SubtitleUIState.STYLE_UPDATING
                    })
            }) {
            drawRect(
                color = Color.Transparent,
                size = state.value.size,
                topLeft = state.value.topLeft,
                style = Fill,
                blendMode = BlendMode.Clear
            )
        }

        val menuExpanded = remember {
            mutableStateOf(true)
        }
        if (uiState.value == OverlayViewModel.SubtitleUIState.STYLE_UPDATING) {
            //测试文字
            Box(modifier = Modifier
                .offset {
                    IntOffset(
                        state.value.topLeft.x.roundToInt(),
                        state.value.topLeft.y.roundToInt()
                    )
                }
                .size(DensityUtil.sizeToDpSize(state.value.size, LocalDensity.current))
                .border(3.dp, MaterialTheme.colorScheme.primary)
                .clickable {
                    menuExpanded.value = !menuExpanded.value;
                }) {
                Text(
                    text = "测试文本1测试文本1测试文本1测试文本1测试文本1测试文本1测试文本1测试文本1测试文本1测试文本1",
                    modifier = Modifier.padding(3.dp),
                    color = state.value.color,
                    fontSize = state.value.fontSize
                )
            }
            //打开字幕菜单
            SubtitleMenu(
                state,
                uiState,
                menuExpanded,
                state.value.topLeft + Offset(state.value.size.width + 20, 0f)
            )
        }
    }
}

@Composable
fun SubtitleMenu(
    state: MutableState<SubtitleSelectState>,
    uiState: MutableState<OverlayViewModel.SubtitleUIState>,
    menuExpanded: MutableState<Boolean>,
    offset: Offset
) {
    Box(modifier = Modifier.offset {
        IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
    }) {
        DropdownMenu(
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "完成") },
                onClick = { uiState.value = OverlayViewModel.SubtitleUIState.SELECTED },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Done,
                        contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text(text = "取消") },
                onClick = { uiState.value = OverlayViewModel.SubtitleUIState.INIT },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = null
                    )
                })
            Divider()
            DropdownMenuItem(
                text = { Text(text = "黑色") },
                onClick = { state.value = state.value.copy(color = Color.Black) },
                leadingIcon = {
                    Box(
                        Modifier
                            .size(14.dp)
                            .background(Color.Black))
                })
            DropdownMenuItem(
                text = { Text(text = "白色") },
                onClick = { state.value = state.value.copy(color = Color.White) },
                leadingIcon = {
                    Box(
                        Modifier
                            .size(14.dp)
                            .background(Color.White))
                })
        }
    }

}
