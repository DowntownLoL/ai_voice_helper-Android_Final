/**
 * @author: Zhang
 * @description: 语音服务: 1.语音提示
 *                       2.语音唤醒显示对话窗口
 *                       3.
 */

package com.example.aivoicehelper.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.aivoicehelper.R
import com.example.aivoicehelper.adapter.ChatListAdapter
import com.example.aivoicehelper.data.ChatList
import com.example.aivoicehelper.entity.AppConstants
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.helper.SoundPoolHelper
import com.example.lib_base.helper.WindowHelper
import com.example.lib_base.helper.`fun`.AppHelper
import com.example.lib_base.helper.`fun`.CommonSettingHelper
import com.example.lib_base.helper.`fun`.ConsTellHelper
import com.example.lib_base.helper.`fun`.ContactHelper
import com.example.lib_base.utils.L
import com.example.lib_network.HttpManager
import com.example.lib_network.bean.JokeOneData
import com.example.lib_network.bean.RobotData
import com.example.lib_network.bean.WeatherData
import com.example.lib_voice.engine.VoiceEngineAnalyze
import com.example.lib_voice.impl.OnAsrResultListener
import com.example.lib_voice.impl.OnNluResultListener
import com.example.lib_voice.manager.VoiceManager
import com.example.lib_voice.tts.VoiceTTS
import com.example.lib_voice.words.WordsTools
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoiceService : Service(), OnNluResultListener {

    private val mHandler = Handler() // 隐藏窗口

    private lateinit var mFullWindowView: View
    private lateinit var mChatListView: RecyclerView
    private lateinit var mLottieView: LottieAnimationView
    private lateinit var ivCloseWindow: ImageView
    private lateinit var tvVoiceTips: TextView

    private val mList = ArrayList<ChatList>()
    private lateinit var mChatAdapter: ChatListAdapter

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        L.i("语音服务启动")
        initCoreVoiceService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bindNotification()
        return START_STICKY_COMPATIBILITY
    }

    // 绑定通知栏
    private fun bindNotification() {
        startForeground(1, NotificationHelper.bindVoiceService("通知栏绑定成功"))
    }

    // 初始化语音服务
    private fun initCoreVoiceService() {

        WindowHelper.initHelper(this)
        mFullWindowView = WindowHelper.getView(R.layout.layout_window_item)
        mChatListView = mFullWindowView.findViewById(R.id.mChatListView) // 聊天框
        mLottieView =
            mFullWindowView.findViewById<LottieAnimationView>(R.id.mLottieView) // 唤醒浮动圆球动画
        tvVoiceTips = mFullWindowView.findViewById<TextView>(R.id.tvVoiceTips) // 提示音
        ivCloseWindow = mFullWindowView.findViewById<ImageView>(R.id.ivCloseWindow)
        mChatListView.layoutManager = LinearLayoutManager(this)
        mChatAdapter = ChatListAdapter(mList)
        mChatListView.adapter = mChatAdapter

        ivCloseWindow.setOnClickListener {
            hideTouchWindow()
        }

        VoiceManager.initManager(this, object : OnAsrResultListener {

            override fun wakeUpReady() {
                L.i("唤醒准备就绪")
                addAiText("唤醒引擎准备就绪")
            }

            override fun asrStartSpeak() {
                L.i("开始说话")
            }

            override fun asrStopSpeak() {
                L.i("结束说话")
            }

            override fun wakeUpSuccess(result: JSONObject) { // result: 唤醒成功：{"errorDesc":"wakup success","errorCode":0,"word":"小度小度"}
                L.i("唤醒成功：$result")
                val errorCode = result.optInt("errorCode")
                // 如果唤醒成功
                if (errorCode == 0) {
                    val word = result.optString("word") // 唤醒词
                    if (word == "小度小度") {    // 当用户说出小度小度时才唤醒语音识别
                        wakeUpFix()
                    }
                }
            }

            override fun updateUserText(text: String) {
                updateTips(text)
            }

            override fun asrResult(result: JSONObject) {
                L.i("====================result=========================")
                L.i("nlu: $result")
            }

            override fun nluResult(nlu: JSONObject) {
                L.i("====================NLU=========================")
                L.i("nlu: $nlu")
                addMineText(nlu.optString("raw_text")) // 使用户的话实时展示在窗口中
                VoiceEngineAnalyze.analyzeNlu(nlu, this@VoiceService)
            }

            override fun voiceError(text: String) {
                L.e("发生错误：$text")
                hideWindow()
            }
        })
    }

    private fun wakeUpFix() {
        showWindow() // 新建窗口
        updateTips("正在聆听...")
        SoundPoolHelper.play(R.raw.record_start)
        // 应答
        val wakeupText = WordsTools.wakeupWords()
        addAiText(wakeupText, object : VoiceTTS.OnTTSResultListener {
            override fun ttsEnd() {
                VoiceManager.startAsr() // 开始识别
            }
        })
    }

    // 显示窗口
    private fun showWindow() {
        L.i("======显示窗口======")
        mLottieView.playAnimation() // LottieView启动动画
        WindowHelper.show(mFullWindowView)
    }

    // 隐藏窗口
    private fun hideWindow() {
        L.i("======隐藏窗口======")
        mHandler.postDelayed({
            WindowHelper.hide(mFullWindowView)
            mLottieView.pauseAnimation() // LottieView暂停动画
//            SoundPoolHelper.play(R.raw.record_over)
        }, 2 * 1000)
    }

    // 直接隐藏窗口
    private fun hideTouchWindow() {
        L.i("======隐藏窗口======")
        WindowHelper.hide(mFullWindowView)
        mLottieView.pauseAnimation()
        SoundPoolHelper.play(R.raw.record_over)
        VoiceManager.stopAsr()
    }

    // 打开App
    override fun openApp(appName: String) {
        if (!TextUtils.isEmpty(appName)) {
            L.i("Open App $appName")
            val isOpen = AppHelper.launcherApp(appName) // 判断能否打开
            if (isOpen) {
                addAiText("正在为你打开$appName")
            } else {
                addAiText("很抱歉，无法打开$appName")
            }
        }
        hideWindow()
    }

    // 卸载App
    override fun uninstallApp(appName: String) {
        if (!TextUtils.isEmpty(appName)) {
            L.i("unInstall App $appName")
            val isUninstall = AppHelper.unInstallApp(appName)
            if (isUninstall) {
                addAiText("正在为你卸载$appName")
            } else {
                addAiText("很抱歉，无法卸载$appName")
            }
        }
        hideWindow()
    }

    // 其他App
    override fun otherApp(appName: String) {
        // 全部跳转应用市场
        if (!TextUtils.isEmpty(appName)) {
            val isIntent = AppHelper.launcherAppStore(appName)
            if (isIntent) {
                addAiText("正在为你打开$appName")
            } else {
                addAiText(WordsTools.noAnswerWords())
            }
        }
    }

    override fun back() {
        addAiText("正在为您执行返回操作")
        CommonSettingHelper.back()
        hideWindow()
    }

    override fun home() {
        addAiText("正在返回主页")
        CommonSettingHelper.home()
        hideWindow()
    }

    override fun setVolumeUp() {
        addAiText("正在增加音量")
        CommonSettingHelper.setVolumeUp()
        hideWindow()
    }

    override fun setVolumeDown() {
        addAiText("正在减小音量")
        CommonSettingHelper.setVolumeDown()
        hideWindow()
    }

    // 星座时间
    override fun conTellTime(name: String) {
        L.i("conTellTime:$name")
        val text = ConsTellHelper.getConsTellTime(name)
        addAiText(text, object : VoiceTTS.OnTTSResultListener {
            override fun ttsEnd() {
                hideWindow()
            }
        })
    }

    // 星座详情
    override fun conTellInfo(name: String) {
        L.i("conTellInfo:$name")
        addAiText(
            "正在为你查询${name}的详情",
            object : VoiceTTS.OnTTSResultListener {
                override fun ttsEnd() {
                    hideWindow()
                }
            })
        ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION, "name", name) // 跳转至星座页面
    }

    // 拨打联系人
    override fun callPhoneForName(name: String) {
        val list = ContactHelper.mContactList.filter { it.phoneName == name }
        if (list.isNotEmpty()) {
//            addAiText(
//                getString(R.string.text_voice_call, name),
//                object : VoiceTTS.OnTTSResultListener {
//                    override fun ttsEnd() {
//                        ContactHelper.callPhone(list[0].phoneNumber)
//                    }
//                })
            addAiText("正在为您呼叫$name", object : VoiceTTS.OnTTSResultListener { // 保证Ai说完话后才调用呼叫
                override fun ttsEnd() {
                    ContactHelper.callPhone(list[0].phoneNumber)
                }
            })
        } else {
//            addAiText(getString(R.string.text_voice_no_friend))
            addAiText("通讯录中没有找到$name")
        }
        hideWindow()
    }

    // 拨打号码
    override fun callPhoneForNumber(phone: String) {
        addAiText("正在为你拨打$phone", object : VoiceTTS.OnTTSResultListener {
            override fun ttsEnd() {
                ContactHelper.callPhone(phone)
            }
        })
        hideWindow()
    }

    // 播放笑话
    override fun playJoke() {
        HttpManager.queryJoke(object : Callback<JokeOneData> {
            override fun onFailure(call: Call<JokeOneData>, t: Throwable) {
                L.i("onFailure:$t")
                jokeError()
            }

            override fun onResponse(call: Call<JokeOneData>, response: Response<JokeOneData>) {
                L.i("Joke onResponse")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.error_code == 0) {
                            // 根据Result随机抽取一段笑话进行播放
                            val index = WordsTools.randomInt(it.result.size)
                            L.i("index:$index")
                            if (index < it.result.size) {
                                val data = it.result[index]
                                addAiText(data.content, object : VoiceTTS.OnTTSResultListener {
                                    override fun ttsEnd() {
                                        hideWindow()
                                    }
                                })
                            }
                        } else {
                            jokeError()
                        }
                    }
                } else {
                    jokeError()
                }
            }
        })
    }

    // 笑话列表
    override fun jokeList() {
//        addAiText(getString(R.string.text_voice_query_joke))
        addAiText("正在为您搜索笑话")
        ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
        hideWindow()
    }

    // 机器人
    override fun aiRobot(text: String) {
        //请求机器人回答
        HttpManager.aiRobotChat(text, object : Callback<RobotData> {

            override fun onFailure(call: Call<RobotData>, t: Throwable) {
                addAiText(WordsTools.noAnswerWords()) // 听不懂
                hideWindow()
            }

            override fun onResponse(
                call: Call<RobotData>,
                response: Response<RobotData>
            ) {
                L.i("机器人结果:" + response.body().toString())
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.intent.code == 10004) {
                            // 回答
                            if (it.results.isEmpty()) {
                                addAiText(WordsTools.noAnswerWords())
                                hideWindow()
                            } else {
                                L.i(it.results[0].values.text)
                                addAiText(it.results[0].values.text)
                                hideWindow()
                            }
                        } else {
                            addAiText(WordsTools.noAnswerWords())
                            hideWindow()
                        }
                    }
                }
            }

        })
    }

    // 无法应答
    override fun nluError() {
        addAiText(WordsTools.noAnswerWords())
        hideWindow()
    }

    // 添加我的文本
    private fun addMineText(text: String) {
        val bean = ChatList(AppConstants.TYPE_MINE_TEXT)
        bean.text = text
        baseAddItem(bean)
    }

    // 添加Ai文本
    private fun addAiText(text: String) {
        val bean = ChatList(AppConstants.TYPE_AI_TEXT)
        bean.text = text
        baseAddItem(bean)
        VoiceManager.ttsStart(text)
    }

    private fun addAiText(text: String, mOnTTSResultListener: VoiceTTS.OnTTSResultListener) {
        val bean = ChatList(AppConstants.TYPE_AI_TEXT)
        bean.text = text
        baseAddItem(bean)
        VoiceManager.ttsStart(text, mOnTTSResultListener)
    }

    // 添加基类
    private fun baseAddItem(bean: ChatList) {
        mList.add(bean)
        mChatAdapter.notifyItemInserted(mList.size - 1)  // 局部刷新 最后一行->size-1
    }

    // 更新提示语
    private fun updateTips(text: String) {
        tvVoiceTips.text = text
    }

    // 笑话Error
    private fun jokeError() {
        hideWindow()
//        addAiText(getString(R.string.text_voice_query_joke_error))
        addAiText("很抱歉，没有搜索到笑话")
    }

    // 查询天气
    override fun queryWeather(city: String) {
        HttpManager.run {
            queryWeather(city, object : Callback<WeatherData> {
                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
//                    addAiText(getString(R.string.text_voice_query_weather_error, city))
                    addAiText("无法为您查询${city}的天气")
                    hideWindow()
                }

                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            it.result.realtime.apply {
                                //在UI上显示
                                addWeather(
                                    city,
                                    wid,
                                    info,
                                    temperature,
                                    object : VoiceTTS.OnTTSResultListener {
                                        override fun ttsEnd() {
                                            hideWindow()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            })
        }
    }

    //天气详情
    override fun queryWeatherInfo(city: String) {
        addAiText("正在为您查询${city}的天气")
        ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER, "city", city)
        hideWindow()
    }

    /**
     * 添加天气
     */
    private fun addWeather(
        city: String, wid: String, info: String,
        temperature: String, mOnTTSResultListener: VoiceTTS.OnTTSResultListener
    ) {
        val bean = ChatList(AppConstants.TYPE_AI_WEATHER)
        bean.city = city
        bean.wid = wid
        bean.info = info
        bean.temperature = "$temperature°"
        baseAddItem(bean)
        val text = city + "今天天气" + info + temperature + "°"
        VoiceManager.ttsStart(text, mOnTTSResultListener)
    }

    override fun playJoke(text: String) {
        HttpManager.queryJoke(object : Callback<JokeOneData> {
            override fun onFailure(call: Call<JokeOneData>, t: Throwable) {
                L.i("onFailure:$t")
                jokeError()
            }

            override fun onResponse(call: Call<JokeOneData>, response: Response<JokeOneData>) {
                L.i("Joke onResponse")
                addAiText(text, object : VoiceTTS.OnTTSResultListener {
                    override fun ttsEnd() {
                        hideWindow()
                    }
                })
            }
        })
    }

}