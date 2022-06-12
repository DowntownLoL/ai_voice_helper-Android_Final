/**
 * @author: Zhang
 * @profile: 通知栏选项的设置 Notification
 * @note: runInitService() -> init渠道
 *                         -> MainActivity -> OnCreate -> run VoiceService()
 *       init渠道和MainActivity存在异步操作会报错（FATAL EXCEPTION）
 *       解决方法：init方法在BaseApp中实现
 */
package com.example.lib_base.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationHelper {

    private lateinit var mContext: Context
    private lateinit var nm: NotificationManager

    private const val CHANNER_ID = "ai_voice_service"
    private const val CHANNER_NAME = "语音服务"

    fun initHelp(mContext: Context) {
        this.mContext = mContext
        nm = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 创建渠道
        setBindVoiceChannel()
    }

    // 设置绑定服务的渠道
    fun setBindVoiceChannel() {
        // Android8.0以上版本才智齿
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNER_ID, CHANNER_NAME, NotificationManager.IMPORTANCE_HIGH)
            // 呼吸灯
            channel.enableLights(false)
            // 震动
            channel.enableVibration(false)
            // 角标
            channel.setShowBadge(false)
            nm.createNotificationChannel(channel)
        }
    }

    // 绑定语音服务
    fun bindVoiceService(contentText: String): Notification {
        // 通知栏对象
        val notificationCompat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(mContext, CHANNER_ID)
        } else {
            NotificationCompat.Builder(mContext)
        }
        // 设置标题
        notificationCompat.setContentTitle(CHANNER_NAME)
        // 设置描述
        notificationCompat.setContentText(contentText)
        // 设置时间
        notificationCompat.setWhen(System.currentTimeMillis())
        // 禁止左滑or右滑
        notificationCompat.setAutoCancel(false)
        return notificationCompat.build()
    }

}