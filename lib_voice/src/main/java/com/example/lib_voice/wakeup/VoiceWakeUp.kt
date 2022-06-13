/**
 * @author: Zhang
 * @description: 语音唤醒App 唤醒词： 小度小度
 * @link: https://ai.baidu.com/ai-doc/SPEECH/Pkgt4wwdx
 */

package com.example.lib_voice.wakeup

import android.content.Context
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import org.json.JSONObject


object VoiceWakeUp {

    private lateinit var wakeUpJson: String
    private lateinit var wp: EventManager

    // 初始化唤醒
    fun initWakeUp(mContext: Context, listener: EventListener) {

        val map = HashMap<Any, Any>()
        // 百度官方导出的唤醒词wakeup.bin文件
        map[SpeechConstant.WP_WORDS_FILE] = "assets:///WakeUp.bin"

        // 是否获取音量
        map[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        // 转换成json
        wakeUpJson = JSONObject(map as Map<Any, Any>).toString()
        // 设置监听器
        wp = EventManagerFactory.create(mContext, "wp")
        wp.registerListener(listener)

//        startWakeUp()

    }

    // 启动唤醒
    fun startWakeUp() {
        Log.i("VoiceManager", wakeUpJson)
        wp.send(SpeechConstant.WAKEUP_START, wakeUpJson, null, 0, 0)
    }

    // 停止唤醒
    fun stopWakeUp() {
        wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0)
    }


}