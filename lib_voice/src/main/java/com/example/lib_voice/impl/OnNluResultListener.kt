/**
 * @author: Zhang
 * @description: AppManager语音指令操作接口
 */
package com.example.lib_voice.impl

interface OnNluResultListener {

    // 查询天气
    fun queryWeather(city:String)

    // 天气信息
    fun queryWeatherInfo(city:String)

    // 打开App
    fun openApp(appName: String)

    // 卸载App
    fun uninstallApp(appName: String)

    // 其他操作
    fun otherApp(appName: String)

    // 返回
    fun back()

    // 主页
    fun home()

    // 音量+
    fun setVolumeUp()

    // 音量-
    fun setVolumeDown()

    // 拨打联系人
    fun callPhoneForName(name: String)

    // 拨打电话
    fun callPhoneForNumber(phone: String)

    // 播放笑话
    fun playJoke()

    // 笑话列表
    fun jokeList()

    // 星座时间
    fun conTellTime(name: String)

    // 星座详情
    fun conTellInfo(name: String)

    // 机器人
    fun aiRobot(text:String)

    // 无法识别客户的话
    fun nluError()
}