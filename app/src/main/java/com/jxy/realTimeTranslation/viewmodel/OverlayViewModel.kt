package com.jxy.realTimeTranslation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.jxy.realTimeTranslation.audio.AudioRecorder
import com.jxy.realTimeTranslation.bean.OverlayState
import com.jxy.realTimeTranslation.bean.SubtitleSelectState
import com.jxy.realTimeTranslation.bean.WindowSize
import com.jxy.realTimeTranslation.core.Recorder

class OverlayViewModel : ViewModel(), Recorder {

    enum class UiState {
        UN_PLAY,
        SELECT,
        PLAY
    }

    enum class SelectUiState {
        SELECTING,
        SELECTED
    }

    enum class SubtitleUIState {
        INIT,
        SELECTING,
        STYLE_UPDATING,
        SELECTED,
    }

    var subtitleSelectState = mutableStateOf(SubtitleSelectState())
    var subtitleUIState = mutableStateOf(SubtitleUIState.INIT)

    val overlayState =
        mutableStateOf(
            OverlayState(
                windowSize = WindowSize.warpWindowSize(),
                alpha = 1f,
                Offset.Zero
            )
        )

    var uiState = mutableStateOf(UiState.UN_PLAY)
    var selectUiState = mutableStateOf(SelectUiState.SELECTING)
    fun startTranslateAudioRecord() {
        AudioRecorder.readFlag = true
        AudioRecorder.readListening(this)
    }

    fun stopTranslateAudioRecord() {
        AudioRecorder.readFlag = false
    }

    /**
     * 处理结果
     */
    override fun audioRecordResult(byteArray: ByteArray) {
        val iterator = byteArray.iterator()
        while (iterator.hasNext()) {
            print(iterator.nextByte())
        }
        println()
    }

    fun stopRecord() {
        AudioRecorder.stopRecording()
    }
}