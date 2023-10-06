package com.jxy.realTimeTranslation.viewmodel

import android.content.Context
import android.media.AudioPlaybackCaptureConfiguration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import com.jxy.realTimeTranslation.audio.AudioRecorder

class HomeScreenViewModel : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.Q)
    @RequiresPermission(value = "android.permission.RECORD_AUDIO")
    fun startRecord(
        context: Context,
        audioPlayBackCaptureConfiguration: AudioPlaybackCaptureConfiguration
    ) {
        AudioRecorder.init(context,audioPlayBackCaptureConfiguration)
        AudioRecorder.startRecording()
    }
}