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
import com.example.lib_voice.asr.VoiceAsr
import com.example.lib_voice.impl.OnAsrResultListener
import com.example.lib_voice.tts.VoiceTTS
import com.example.lib_voice.wakeup.VoiceWakeUp
import com.example.lib_voice.words.WordsTools
import org.json.JSONObject

object VoiceManager : EventListener {

    private var TAG = VoiceManager::class.java.simpleName

    // 百度后台注册的密钥
    const val VOICE_APP_ID = "26390086"
    const val VOICE_APP_KEY = "I1Y5SKxE5oaSfBTXCqg0RhV4"
    const val VOICE_APP_SECRET = "np3AamYRnSNNDnxHulcmMKWqLiQdt16P"

    // 接口
    private lateinit var mOnAsrResultListener: OnAsrResultListener

    fun initManager(mContext: Context, mOnAsrResultListener: OnAsrResultListener) {
        this.mOnAsrResultListener = mOnAsrResultListener

        VoiceTTS.initTTS(mContext)
        VoiceWakeUp.initWakeUp(mContext, this)
        VoiceAsr.initAsr(mContext, this)
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

    //开始识别
    fun startAsr() {
        VoiceAsr.startAsr()
    }

    //停止识别
    fun stopAsr() {
        VoiceAsr.stopAsr()
    }

    //取消识别
    fun cancelAsr() {
        VoiceAsr.cancelAsr()
    }

    //销毁
    fun releaseAsr() {
        VoiceAsr.releaseAsr(this)
    }

    override fun onEvent(
        name: String?,
        params: String?,
        byte: ByteArray?,
        offset: Int,
        length: Int
    ) {
//        Log.d(TAG, String.format("event: name=%s, params=%s", name, params))
        when (name) {
//            SpeechConstant.CALLBACK_EVENT_WAKEUP_READY -> Log.i(TAG, "唤醒准备就绪")
//            SpeechConstant.CALLBACK_EVENT_ASR_BEGIN -> Log.i(TAG, "开始说话")
//            SpeechConstant.CALLBACK_EVENT_ASR_END -> Log.i(TAG, "结束说话")
            SpeechConstant.CALLBACK_EVENT_WAKEUP_READY -> mOnAsrResultListener.wakeUpReady()
            SpeechConstant.CALLBACK_EVENT_ASR_BEGIN -> mOnAsrResultListener.asrStartSpeak()
            SpeechConstant.CALLBACK_EVENT_ASR_END -> mOnAsrResultListener.asrStopSpeak()

        }

        if (params == null) {
            return
        }

        val allJson = JSONObject(params) // params转换为json 获取识别语音信息
        Log.i("Test", "allJson$name$allJson")
        when (name) {
            SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS -> mOnAsrResultListener.wakeUpSuccess(
                allJson
            )
            SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR -> mOnAsrResultListener.voiceError("唤醒失败")
            SpeechConstant.CALLBACK_EVENT_ASR_FINISH -> mOnAsrResultListener.asrResult(allJson)
            SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL -> {
                mOnAsrResultListener.updateUserText(allJson.optString("best_result")) // 获取最终文本
                byte?.let {
                    val nlu = JSONObject(String(byte, offset, length))
                    mOnAsrResultListener.nluResult(nlu)
                }
            }
        }
    }


}