package com.jxy.realTimeTranslation.audio

import android.content.ContentValues
import android.content.Context
import android.util.Log
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.StorageService
import java.io.IOException

object VoskModel {
    private var model: Model? = null
    var recognizer: Recognizer? = null
    fun init(context: Context) {
        StorageService.unpack(
            context,
            "model-en-us",
            "model",
            { model: Model? ->
                this.model = model
                recognizer = Recognizer(model, AudioRecorder.sampleRate.toFloat())
            }) { e: IOException ->
            Log.e(
                ContentValues.TAG,
                "VoskModel: " + e.message
            )
        }
    }

    /**
     * 读取音频数据
     */
    fun readData(buffer: ByteArray, len: Int?) {
        if (recognizer?.acceptWaveForm(buffer, len!!) == true) {
            println(recognizer?.result)
        } else {
            println(recognizer?.partialResult)
        }
        println(recognizer?.finalResult)
    }

}