package com.jxy.realTimeTranslation.ui.screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioPlaybackCaptureConfiguration
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jxy.realTimeTranslation.service.OverlayService
import com.jxy.realTimeTranslation.viewmodel.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun HomeScreen(homeScreenViewModel: HomeScreenViewModel = viewModel()) {
    val context = LocalContext.current
    val overlayPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    val mediaProjectionManager =
        context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    val recordPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {

        } else {
            // 用户拒绝了录音权限请求，可能需要给他提示或者其他处理
            Toast.makeText(context, "拒绝后无法获取音频", Toast.LENGTH_SHORT).show()
        }
    }
    val mediaPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            // 获取MediaProjection对象并开始屏幕截图
            val mMediaProjection =
                mediaProjectionManager.getMediaProjection(result.resultCode, data!!)
            val audioPlayBackCaptureConfiguration =
                AudioPlaybackCaptureConfiguration.Builder(mMediaProjection)
                    .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
                    .addMatchingUsage(AudioAttributes.USAGE_UNKNOWN)
                    .addMatchingUsage(AudioAttributes.USAGE_GAME)
                    .build()
            // 录音权限已授予，可以进行录音了
            homeScreenViewModel.startRecord(context, audioPlayBackCaptureConfiguration)
        } else {
            // 用户拒绝了录音权限请求，可能需要给他提示或者其他处理
            Toast.makeText(context, "拒绝后无法获取音频", Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        FilledTonalButton(onClick = {
            if (Settings.canDrawOverlays(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    recordPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                //启动悬浮窗服务
                context.startService(
                    Intent(
                        context,
                        OverlayService::class.java
                    )
                )
                //开启捕获音频
                mediaPermissionLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                overlayPermissionLauncher.launch(intent)
            }
        }) {
            Text(text = "开启悬浮窗")
        }
    }

}

@RequiresApi(Build.VERSION_CODES.R)
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}