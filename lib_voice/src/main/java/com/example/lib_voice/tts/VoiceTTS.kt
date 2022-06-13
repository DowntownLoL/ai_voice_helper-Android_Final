/**
 * @author: Zhang
 * @description: 百度TTS封装
 * @link: 百度官方文档： https://ai.baidu.com/ai-doc/SPEECH/Yk843u2m5
 */

package com.example.lib_voice.tts

import android.content.Context
import android.util.Log
import com.baidu.tts.client.SpeechError
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.SpeechSynthesizerListener
import com.baidu.tts.client.TtsMode
import com.example.lib_voice.manager.VoiceManager

object VoiceTTS : SpeechSynthesizerListener {

    private var TAG = VoiceTTS::class.java.simpleName

    private lateinit var mSpeechSynthesizer: SpeechSynthesizer  // 获取 SpeechSynthesizer 实例

    // 接口对象
    private var mOnTTSResultListener: OnTTSResultListener? = null

    // 初始化
    fun initTTS(mContext: Context) {
        // 初始化对象
        mSpeechSynthesizer = SpeechSynthesizer.getInstance()
        // 设置当前的Context
        mSpeechSynthesizer.setContext(mContext); // this 是Context的之类，如Activity
        // 设置key
        mSpeechSynthesizer.setAppId(VoiceManager.VOICE_APP_ID)
        mSpeechSynthesizer.setApiKey(VoiceManager.VOICE_APP_KEY, VoiceManager.VOICE_APP_SECRET)
        // 设置监听
        mSpeechSynthesizer.setSpeechSynthesizerListener(this)

        // 初始化声音属性
        // 发声人
        setPeople("5118")
        // 语速
        setSpeed("6")
        // 音量
        setVoiceVolume("8")
        // 初始化
        mSpeechSynthesizer.initTts(TtsMode.ONLINE)  // 纯在线
    }

    // 设置发音人
    fun setPeople(people: String) {
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, people)
    }

    // 设置语速
    fun setSpeed(speed: String) {
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, speed)
    }

    // 设置音量
    fun setVoiceVolume(volume: String) {
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, volume)
    }

    override fun onSynthesizeStart(p0: String?) {
        Log.i(TAG, "合成开始")
    }

    override fun onSynthesizeDataArrived(p0: String?, p1: ByteArray?, p2: Int, p3: Int) {

    }

    override fun onSynthesizeFinish(p0: String?) {

    }

    override fun onSpeechStart(p0: String?) {
        Log.i(TAG, "开始播放")
    }

    override fun onSpeechProgressChanged(p0: String?, p1: Int) {

    }

    override fun onSpeechFinish(p0: String?) {
        Log.i(TAG, "播放结束")
        mOnTTSResultListener?.ttsEnd()
    }

    override fun onError(p0: String?, p1: SpeechError?) {
        Log.i(TAG, "TTS Error!: $p0:$p1")
    }

    // 播放text并且有回调
    fun start(text: String, mOnTTSResultListener: OnTTSResultListener?) {
        this.mOnTTSResultListener = mOnTTSResultListener
        mSpeechSynthesizer.speak(text)
    }

    // 暂停
    fun pause() {
        mSpeechSynthesizer.pause()
    }

    // 继续播放
    fun resume() {
        mSpeechSynthesizer.resume()
    }

    // 终止播放
    fun stop() {
        mSpeechSynthesizer.stop()
    }

    // 释放资源
    fun release() {
        mSpeechSynthesizer.release()
    }

    // 接口
    interface OnTTSResultListener {

        // 播放结束
        fun ttsEnd()
    }
}