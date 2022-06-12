/**
 * @author: Zhang
 * @profile: 语音管理类封装
 */
package com.example.lib_voice.manager

import android.content.Context
import com.example.lib_voice.tts.VoiceTTS

object VoiceManager {

    // 百度后台注册的密钥
    const val VOICE_APP_ID = "26390086"
    const val VOICE_APP_KEY = "I1Y5SKxE5oaSfBTXCqg0RhV4"
    const val VOICE_APP_SECRET = "np3AamYRnSNNDnxHulcmMKWqLiQdt16P"

    fun initManager(mContext: Context) {
        VoiceTTS.initTTS(mContext)
    }

    // TTS Start
    // 播放text
    fun start(text: String) {
        VoiceTTS.start(text)
    }

    // 暂停
    fun pause() {
        VoiceTTS.pause()
    }

    // 继续播放
    fun resume() {
        VoiceTTS.resume()
    }

    // 终止播放
    fun stop() {
        VoiceTTS.stop()
    }

    // 释放资源
    fun release() {
        VoiceTTS.release()
    }

    // TTS End
}