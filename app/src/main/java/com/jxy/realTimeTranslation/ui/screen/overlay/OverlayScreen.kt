package com.jxy.realTimeTranslation.ui.screen.overlay

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jxy.realTimeTranslation.bean.SubtitleSelectState
import com.jxy.realTimeTranslation.bean.WindowSize
import com.jxy.realTimeTranslation.viewmodel.OverlayViewModel
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun OverlayScreen(viewModel: OverlayViewModel = viewModel()) {
    val uiState = remember {
        viewModel.uiState
    }

    val subtitleSelectState = remember {
        viewModel.subtitleSelectState
    }

    when (uiState.value) {
        OverlayViewModel.UiState.UN_PLAY -> {
            viewModel.overlayState.value =
                viewModel.overlayState.value.copy(windowSize = WindowSize.warpWindowSize())
            UnPlay(uiState)
        }
        OverlayViewModel.UiState.SELECT -> {
            Select(uiState, viewModel)
        }
        OverlayViewModel.UiState.PLAY -> {
            viewModel.overlayState.value = viewModel.overlayState.value.copy(
                windowSize = WindowSize.sizeToWindowSize(subtitleSelectState.value.size)
            )
            Play(subtitleSelectState, viewModel)
        }
    }
}

@Composable
fun UnPlay(uiState: MutableState<OverlayViewModel.UiState>) {
    FilledTonalButton(onClick = { uiState.value = OverlayViewModel.UiState.SELECT }) {
        Text(text = "设置字幕")
    }
}


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Play(
    subtitleSelectState: MutableState<SubtitleSelectState>,
    viewModel: OverlayViewModel,
) {
    val menuExpanded = remember {
        mutableStateOf(false)
    }
    Box(
        Modifier
            .fillMaxSize()
            .clickable { menuExpanded.value = !menuExpanded.value }
    ) {
        Text(
            text = "测试字幕字幕字幕",
            color = subtitleSelectState.value.color,
            fontSize = subtitleSelectState.value.fontSize
        )
    }
    PlayMenu(
        viewModel,
        state = subtitleSelectState,
        menuExpanded = menuExpanded,
        offset = Offset(subtitleSelectState.value.size.width, 0f)
    )
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun PlayMenu(
    viewModel: OverlayViewModel,
    state: MutableState<SubtitleSelectState>,
    menuExpanded: MutableState<Boolean>,
    offset: Offset
) {
    val context = LocalContext.current
    Box(modifier = Modifier.offset {
        IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
    }) {
        DropdownMenu(
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "开始") },
                onClick = {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(context, "请开启录音权限", Toast.LENGTH_SHORT).show()
                    }
                    viewModel.startTranslateAudioRecord()
                },
                leadingIcon = {
                    Icon(Icons.Outlined.Refresh, contentDescription = "")
                })
            DropdownMenuItem(
                text = { Text(text = "暂停") },
                onClick = {
                    viewModel.stopTranslateAudioRecord()
                },
                leadingIcon = {
                    Icon(Icons.Outlined.Refresh, contentDescription = "")
                })
            DropdownMenuItem(
                text = { Text(text = "退出") },
                onClick = {
                    viewModel.stopRecord()
                },
                leadingIcon = {
                    Icon(Icons.Outlined.Refresh, contentDescription = "")
                })

            DropdownMenuItem(
                text = { Text(text = "重新设置") },
                onClick = { },
                leadingIcon = {
                    Icon(Icons.Outlined.Refresh, contentDescription = "")
                })
            Divider()
            DropdownMenuItem(
                text = { Text(text = "黑色") },
                onClick = { state.value = state.value.copy(color = Color.Black) },
                leadingIcon = {
                    Box(
                        Modifier
                            .size(14.dp)
                            .background(Color.Black)
                    )
                })
            DropdownMenuItem(
                text = { Text(text = "白色") },
                onClick = { state.value = state.value.copy(color = Color.White) },
                leadingIcon = {
                    Box(
                        Modifier
                            .size(14.dp)
                            .background(Color.White)
                    )
                })
        }
    }

}
