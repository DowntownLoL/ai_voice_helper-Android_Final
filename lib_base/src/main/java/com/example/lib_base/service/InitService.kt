/**
 * @author: Zhang
 * @profile: IntentService 初始化服务
 */

package com.example.lib_base.service

import android.app.IntentService
import android.content.Intent
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.utils.L
import com.example.lib_base.utils.SpUtils
import com.example.lib_voice.manager.VoiceManager

class InitService:IntentService(InitService::class.simpleName){

    override fun onCreate() {
        super.onCreate()
        L.i("InitService启动")
    }

    override fun onHandleIntent(intent: Intent?) {
        L.i("初始化")

        SpUtils.initUtils(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        L.i("初始化完成")
    }
}
