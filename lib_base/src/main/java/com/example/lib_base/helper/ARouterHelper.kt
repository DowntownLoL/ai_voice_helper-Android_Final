/**
 * @author: Zhang
 * @description: 路由帮助类
 * @link: https://github.com/alibaba/ARouter
 */
package com.example.lib_base.helper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.BuildConfig

object ARouterHelper {
    // Module First Run Path
    const val PATH_APP_MANAGER = "/app_manager/app_manager_activity"
    const val PATH_CONSTELLATION = "/constellation/constellation_activity"
    const val PATH_DEVELOPER = "/developer/developer_activity"
    const val PATH_JOKE = "/joke/joke_activity"
    const val PATH_MAP = "/map/map_activity"
    const val PATH_SETTING = "/setting/setting_activity"
    const val PATH_VOICE_SETTING = "/voice/voice_setting_activity"
    const val PATH_WEATHER = "/weather/weather_activity"

    // 初始化SDK
    fun initHelper(application: Application) {
        if (BuildConfig.DEBUG) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog()     // Print log
            ARouter.openDebug()   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(application) // As early as possible, it is recommended to initialize in the Application
    }

    // 跳转页面
    fun startActivity(path: String) {
        // 1. Simple jump within application (Jump via URL in 'Advanced usage')
        ARouter.getInstance().build(path).navigation()
    }

    // 跳转页面 requestCode
    fun startActivity(activity: Activity, path: String, requestCode: Int) {
        // 1. Simple jump within application (Jump via URL in 'Advanced usage')
        ARouter.getInstance().build(path).navigation(activity, requestCode)
    }

    //String
    fun startActivity(path: String, key: String, value: String) {
        // 2. Jump with parameters
        ARouter.getInstance().build(path).withString(key, value).navigation()
    }

    // Int
    fun startActivity(path: String, key: String, value: Int) {
        // 2. Jump with parameters
        ARouter.getInstance().build(path).withInt(key, value).navigation()
    }

    // Boolean
    fun startActivity(path: String, key: String, value: Boolean) {
        // 2. Jump with parameters
        ARouter.getInstance().build(path).withBoolean(key, value).navigation()
    }

    // Bundle
    fun startActivity(path: String, key: String, bundle: Bundle) {
        // 2. Jump with parameters
        ARouter.getInstance().build(path).withBundle(key, bundle).navigation()
    }

    // Any(Object)
    fun startActivity(path: String, key: String, any: Any) {
        // 2. Jump with parameters
        ARouter.getInstance().build(path).withObject(key, any).navigation()
    }


}