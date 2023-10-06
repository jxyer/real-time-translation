package com.jxy.realTimeTranslation.audio

import android.content.ContentValues.TAG
import android.content.Context
import android.media.AudioFormat
import android.media.AudioPlaybackCaptureConfiguration
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.jxy.realTimeTranslation.core.Recorder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.vosk.android.SpeechService

object AudioRecorder {
    const val sampleRate = 16000
    private const val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private const val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val blockSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    private var audioRecord: AudioRecord? = null

    //读取标识 false:不读 true:读
    var readFlag: Boolean = false

    @RequiresApi(Build.VERSION_CODES.Q)
    @RequiresPermission(value = "android.permission.RECORD_AUDIO")
    fun init(
        context: Context,
        audioPlayBackCaptureConfiguration: AudioPlaybackCaptureConfiguration
    ) {
        audioRecord = AudioRecord.Builder()
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setChannelMask(channelConfig)
                    .setEncoding(audioFormat)
                    .build()
            )
            .setBufferSizeInBytes(blockSize)
            .setAudioPlaybackCaptureConfig(audioPlayBackCaptureConfiguration)
            .build()
        VoskModel.init(context)
    }

    fun startRecording() {
        if (audioRecord?.state == AudioRecord.STATE_INITIALIZED) {
            audioRecord?.startRecording()
        }
    }

    private var readJob: Job? = null
    fun readListening(recorder: Recorder) {
        readJob = GlobalScope.launch {
            while (readFlag) {
                val buffer=ByteArray(blockSize)
                val len = audioRecord?.read(buffer, 0, blockSize)
                when (len) {
                    AudioRecord.ERROR_INVALID_OPERATION -> {
                        Log.e(TAG, "read() returned ERROR_INVALID_OPERATION")
                    }
                    AudioRecord.ERROR_BAD_VALUE -> {
                        Log.e(TAG, "read() returned ERROR_BAD_VALUE")
                    }
                    else -> {
                        VoskModel.readData(buffer, len)
                    }
                }
            }
        }
    }

    fun stopRecording() {
        readJob?.cancel()
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    fun isRunning(): Boolean {
        return audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING
    }

}