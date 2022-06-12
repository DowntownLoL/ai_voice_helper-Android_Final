/**
 * @author: Zhang
 * @profile: 语音服务
 */

package com.example.aivoicehelper.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.utils.L
import com.example.lib_voice.manager.VoiceManager

class VoiceService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        L.i("语音服务启动")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bindNotification()
        return START_STICKY_COMPATIBILITY
    }

    // 绑定通知栏
    private fun bindNotification() {
        startForeground(1, NotificationHelper.bindVoiceService("通知栏绑定成功"))
    }
}