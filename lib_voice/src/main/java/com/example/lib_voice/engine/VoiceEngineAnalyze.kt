/**
 * @author: Zhang
 * @description: 语音引擎分析
 */
package com.example.lib_voice.engine

import android.util.Log
import com.example.lib_voice.impl.OnNluResultListener
import com.example.lib_voice.words.NluWrods
import org.json.JSONObject

object VoiceEngineAnalyze {

    private var TAG = VoiceEngineAnalyze::class.java.simpleName

    private lateinit var mOnNluResultListener: OnNluResultListener

    // 分析结果
    fun analyzeNlu(nlu: JSONObject, mOnNluResultListener: OnNluResultListener) {
        this.mOnNluResultListener = mOnNluResultListener
        // 用户说的话
        val rawText = nlu.optString("raw_text")
        Log.i(TAG, "rawText:$rawText")

        // 解析results
        val results = nlu.optJSONArray("results") ?: return
        val nluResultLength = results.length()
        when {
            // 百度ai识别失败 调用图灵robot
            nluResultLength <= 0 || rawText == "你好。" || rawText == "今天星期几？" || rawText == "你今天开心吗？"-> mOnNluResultListener.aiRobot(rawText)
            // 单条命中
            results.length() >= 1 -> analyzeNluSingle(results[0] as JSONObject)
        }
    }

    // 处理单条结果
    private fun analyzeNluSingle(result: JSONObject) {
        val domain = result.optString("domain")
        val intent = result.optString("intent")
        val slots = result.optJSONObject("slots")

        // 根据百度接口返回的json信息进行操作
        slots?.let {
            when (domain) {
                NluWrods.NLU_APP -> {
                    when (intent) {
                        NluWrods.INTENT_OPEN_APP,
                        NluWrods.INTENT_UNINSTALL_APP,
                        NluWrods.INTENT_UPDATE_APP,
                        NluWrods.INTENT_DOWNLOAD_APP,
                        NluWrods.INTENT_SEARCH_APP,
                        NluWrods.INTENT_RECOMMEND_APP -> {
                            val userAppName = it.optJSONArray("user_app_name") // 得到打开App的名称
                            userAppName?.let { appName ->
                                if (appName.length() > 0) {
                                    val obj = appName[0] as JSONObject
                                    val word = obj.optString("word")
                                    when (intent) {
                                        // 打开App指令
                                        NluWrods.INTENT_OPEN_APP -> mOnNluResultListener.openApp(
                                            word
                                        )
                                        // 卸载App指令
                                        NluWrods.INTENT_UNINSTALL_APP -> mOnNluResultListener.uninstallApp(
                                            word
                                        )
                                        // 除打开和卸载外其他App指令
                                        else -> mOnNluResultListener.otherApp(word)
                                    }
                                } else {
                                    mOnNluResultListener.nluError() // 无法识别
                                }
                            }
                        }
                        else -> {
                            // 除打开和卸载外其他App操作
                            mOnNluResultListener.nluError() // 无法识别

                        }
                    }

                } // 搜索、下载、打开App
                NluWrods.NLU_INSTRUCTION -> {
                    when (intent) {
                        NluWrods.INTENT_RETURN -> mOnNluResultListener.back()
                        NluWrods.INTENT_BACK_HOME -> mOnNluResultListener.home()
                        NluWrods.INTENT_VOLUME_UP -> mOnNluResultListener.setVolumeUp()
                        NluWrods.INTENT_VOLUME_DOWN -> mOnNluResultListener.setVolumeDown()
                        else -> mOnNluResultListener.nluError() // 无法识别
                    }
                } // 返回、回到主页
                NluWrods.NLU_MOVIE -> {
                    //音量改变
                    if (NluWrods.INTENT_MOVIE_VOL == intent) {
                        val userD = slots.optJSONArray("user_d")
                        userD?.let { user ->
                            if (user.length() > 0) {
                                val word = (user[0] as JSONObject).optString("word")
                                if (word == "大点") {
                                    mOnNluResultListener.setVolumeUp()
                                } else if (word == "小点") {
                                    mOnNluResultListener.setVolumeDown()
                                }
                            }
                        }
                    } else {
                        mOnNluResultListener.nluError()
                    }
                } // 增大、减小手机音量
                NluWrods.NLU_ROBOT -> {
                    if (NluWrods.INTENT_ROBOT_VOLUME == intent) {
                        val volumeControl = slots.optJSONArray("user_volume_control")
                        volumeControl?.let { control ->
                            val word = (control[0] as JSONObject).optString("word")
                            if (word == "大点") {
                                mOnNluResultListener.setVolumeUp()
                            } else if (word == "小点") {
                                mOnNluResultListener.setVolumeDown()
                            }
                        }
                    } else {
                        mOnNluResultListener.nluError()
                    }
                } // 增大、减小手机音量
                NluWrods.NLU_TELEPHONE -> { // 打电话
                    if (NluWrods.INTENT_CALL == intent) {
                        when {
                            slots.has("user_call_target") -> {  // 接口返回user_call_target数据则打电话
                                val callTarget = slots.optJSONArray("user_call_target")
                                callTarget?.let { target ->
                                    if (target.length() > 0) {
                                        val name = (target[0] as JSONObject).optString("word")
                                        mOnNluResultListener.callPhoneForName(name)
                                    } else {
                                        mOnNluResultListener.nluError()
                                    }
                                }
                            }
                            slots.has("user_phone_number") -> {
                                val phoneNumber = slots.optJSONArray("user_phone_number")
                                phoneNumber?.let { number ->
                                    if (number.length() > 0) {
                                        val phone = (number[0] as JSONObject).optString("word")
                                        mOnNluResultListener.callPhoneForNumber(phone)
                                    } else {
                                        mOnNluResultListener.nluError()
                                    }
                                }
                            }
                            else -> {
                                mOnNluResultListener.nluError()
                            }
                        }
                    } else {
                        mOnNluResultListener.nluError()
                    }
                } // 打电话
                NluWrods.NLU_JOKE -> { // 讲笑话
                    if (intent == NluWrods.INTENT_TELL_JOKE) {
                        mOnNluResultListener.playJoke()
                    } else {
                        mOnNluResultListener.jokeList()
                    }
                } // 讲笑话
                NluWrods.NLU_SEARCH, NluWrods.NLU_NOVEL -> {
                    if (intent == NluWrods.INTENT_SEARCH) {
                        mOnNluResultListener.jokeList()
                    } else {
                        mOnNluResultListener.nluError()
                    }
                } // 搜索笑话
                NluWrods.NLU_CONSTELL -> {
                    val consTellNameArray = slots.optJSONArray("user_constell_name")
                    consTellNameArray?.let { consTell ->
                        if (consTell.length() > 0) {
                            val wordObject = consTell[0] as JSONObject
                            val word = wordObject.optString("word")
                            when (intent) {
                                NluWrods.INTENT_CONSTELL_TIME -> mOnNluResultListener.conTellTime(
                                    word
                                )
                                NluWrods.INTENT_CONSTELL_INFO -> mOnNluResultListener.conTellInfo(
                                    word
                                )
                                else -> mOnNluResultListener.nluError()
                            }
                        }
                    }
                } // 搜索星座
                NluWrods.NLU_WEATHER -> {
                    val userLoc = slots.optJSONArray("user_loc")
                    userLoc?.let { loc ->
                        if (loc.length() > 0) {
                            val locObject = loc[0] as JSONObject
                            val word = locObject.optString("word")
                            if (intent == NluWrods.INTENT_USER_WEATHER) {
                                mOnNluResultListener.queryWeather(word)
                            } else {
                                mOnNluResultListener.queryWeatherInfo(word)
                            }
                        }
                    }
                }
                else -> mOnNluResultListener.nluError() // 无法识别
            }
        }

    }
}