/**
 * @author: Zhang
 * @description: 基类App
 */

package com.example.lib_base.base

import android.app.Application
import android.content.Intent
import android.os.Build
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.service.InitService
import com.example.lib_base.utils.SpUtils
import com.example.lib_voice.manager.VoiceManager

open class BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()

        ARouterHelper.initHelper(this)
        NotificationHelper.initHelp(this)
        startService(Intent(this, InitService::class.java))
    }
}