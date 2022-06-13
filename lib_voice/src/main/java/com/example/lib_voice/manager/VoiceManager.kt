/**
 * @author: Zhang
 * @description: 语音管理类封装
 */
package com.example.lib_voice.manager

import android.content.Context
import android.nfc.Tag
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import com.baidu.tts.client.SpeechSynthesizer
import com.example.lib_voice.tts.VoiceTTS
import com.example.lib_voice.wakeup.VoiceWakeUp

object VoiceManager : EventListener {

    private var TAG = VoiceManager::class.java.simpleName

    // 百度后台注册的密钥
    const val VOICE_APP_ID = "26390086"
    const val VOICE_APP_KEY = "I1Y5SKxE5oaSfBTXCqg0RhV4"
    const val VOICE_APP_SECRET = "np3AamYRnSNNDnxHulcmMKWqLiQdt16P"

    fun initManager(mContext: Context) {
        VoiceTTS.initTTS(mContext)
        VoiceWakeUp.initWakeUp(mContext, this)
    }

    // 播放text
    fun ttsStart(text: String) {
        Log.d(TAG, "开始TTS：$text")
        VoiceTTS.start(text, null)
    }

    // 播放text且监听结果
    fun ttsStart(text: String, mOnTTSResultListener: VoiceTTS.OnTTSResultListener) {
        Log.d(TAG, "开始TTS-m：$text")
        VoiceTTS.start(text, mOnTTSResultListener)
    }

    // 暂停
    fun ttsPause() {
        VoiceTTS.pause()
    }

    // 继续播放
    fun ttsResume() {
        VoiceTTS.resume()
    }

    // 终止播放
    fun ttsStop() {
        VoiceTTS.stop()
    }

    // 释放资源
    fun ttsRelease() {
        VoiceTTS.release()
    }

    // 设置发音人
    fun setPeople(people: String) {
        VoiceTTS.setPeople(people)
    }

    // 设置语速
    fun setSpeed(speed: String) {
        VoiceTTS.setSpeed(speed)
    }

    // 设置音量
    fun setVoiceVolume(volume: String) {
        VoiceTTS.setVoiceVolume(volume)
    }

    // 启动唤醒
    fun startWakeUp() {
        Log.d(TAG, "启动唤醒")
        VoiceWakeUp.startWakeUp()
    }

    // 停止唤醒
    fun stopWakeUp() {
        VoiceWakeUp.stopWakeUp()
    }

    override fun onEvent(name: String?, params: String?, byte: ByteArray?, p3: Int, p4: Int) {
        Log.d(TAG, String.format("event: name=%s, params=%s", name, params))
        name?.let {
            when (it) {
                SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS -> ttsStart("我在")
            }
        }
    }


}