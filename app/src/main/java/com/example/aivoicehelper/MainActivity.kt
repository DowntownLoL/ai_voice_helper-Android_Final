package com.example.aivoicehelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.example.aivoicehelper.databinding.ActivityMainBinding
import com.example.lib_base.event.EventManager
import com.example.lib_base.event.MessageEvent
import com.example.lib_base.helper.ARouterHelper
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)
        setContentView(binding.root) // viewbinding需要把R.layout.xx -> binding.root

//        EventManager.register(this) // EventBus 注册

//        binding.btn1.setOnClickListener() {
//            EventManager.post(1111)
//        }
//
//        binding.btn2.setOnClickListener() {
//            EventManager.post(2222, "Hello World")
//        }

        binding.btn1.setOnClickListener() {
            ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        EventManager.unRegister(this) // EventBus 解绑
    }

    // EventBus prepare subscribers
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onMessageEvent(event: MessageEvent) {
//        when(event.type) {
//            1111-> Log.i("TestApp", "1111")
//            2222-> Log.i("TestApp", event.stringValue)
//        }
//    }

}