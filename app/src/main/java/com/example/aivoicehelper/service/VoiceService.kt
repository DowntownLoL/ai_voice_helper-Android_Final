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
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.helper.SoundPoolHelper
import com.example.lib_base.helper.WindowHelper
import com.example.lib_base.utils.L
import com.example.lib_voice.engine.VoiceEngineAnalyze
import com.example.lib_voice.impl.OnAsrResultListener
import com.example.lib_voice.impl.OnNluResultListener
import com.example.lib_voice.manager.VoiceManager
import com.example.lib_voice.tts.VoiceTTS
import com.example.lib_voice.words.WordsTools
import org.json.JSONObject

class VoiceService : Service(), OnNluResultListener {

    private val mHandler = Handler() // 隐藏窗口

    private lateinit var mFullWindowView: View
    private lateinit var mChatListView: RecyclerView
    private lateinit var mLottieView: LottieAnimationView
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
        mLottieView = mFullWindowView.findViewById<LottieAnimationView>(R.id.mLottieView) // 唤醒浮动圆球动画
        tvVoiceTips = mFullWindowView.findViewById<TextView>(R.id.tvVoiceTips) // 提示音
        mChatListView.layoutManager = LinearLayoutManager(this)
        mChatAdapter = ChatListAdapter(mList)
        mChatListView.adapter = mChatAdapter

        VoiceManager.initManager(this, object : OnAsrResultListener {

            override fun wakeUpReady() {
                L.i("唤醒准备就绪")
                VoiceManager.ttsStart("唤醒引擎准备就绪")
            }

            override fun asrStartSpeak() {
                L.i("开始说话")
            }

            override fun asrStopSpeak() {
                L.i("结束说话")
                hideWindow()
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
                addAiText(nlu.toString())
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
        addAiText(wakeupText)
        VoiceManager.ttsStart(wakeupText,
            object : VoiceTTS.OnTTSResultListener {
                override fun ttsEnd() {
                    VoiceManager.startAsr()
                }
            })
    }

    //显示窗口
    private fun showWindow() {
        L.i("======显示窗口======")
        mLottieView.playAnimation() // LottieView启动动画
        WindowHelper.show(mFullWindowView)
    }

    //隐藏窗口
    private fun hideWindow() {
        L.i("======隐藏窗口======")
        mHandler.postDelayed({
            WindowHelper.hide(mFullWindowView)
            mLottieView.pauseAnimation() // LottieView暂停动画
//            SoundPoolHelper.play(R.raw.record_over)
        }, 2 * 1000)
    }

    // 查询天气
    override fun queryWeather() {

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
//        VoiceManager.ttsStart(text)
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
}