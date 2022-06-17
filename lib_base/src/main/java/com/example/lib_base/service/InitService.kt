/**
 * @author: Zhang
 * @description: IntentService 初始化服务
 */

package com.example.lib_base.service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.helper.SoundPoolHelper
import com.example.lib_base.helper.`fun`.AppHelper
import com.example.lib_base.helper.`fun`.CommonSettingHelper
import com.example.lib_base.helper.`fun`.ConsTellHelper
import com.example.lib_base.utils.AssetsUtils
import com.example.lib_base.utils.L
import com.example.lib_base.utils.SpUtils
import com.example.lib_voice.manager.VoiceManager
import com.example.lib_voice.words.WordsTools

class InitService:IntentService(InitService::class.simpleName){

    override fun onCreate() {
        super.onCreate()
//        L.i("InitService启动")
    }

    override fun onHandleIntent(intent: Intent?) {
        L.i("初始化")

        SpUtils.initUtils(this)
        WordsTools.initTools(this)
        SoundPoolHelper.init(this)
        AppHelper.initHelper(this)
        CommonSettingHelper.initHelper(this)
        ConsTellHelper.initHelper(this)
        AssetsUtils.initUtils(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        L.i("初始化完成")
    }
}
