package com.example.aivoicehelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aivoicehelper.service.VoiceService
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.lib_base.event.EventManager
import com.example.lib_base.event.MessageEvent
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.utils.L
import com.example.lib_base.utils.SpUtils
import com.yanzhenjie.permission.AndPermission
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlinx.android.synthetic.main.activity_main.*
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.runtime.Permission

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getTitleText(): String {
        return getString(R.string.app_name)
    }

    override fun isShowBack(): Boolean {
        return false // 主页不设置返回键
    }

    override fun initView() {
        startService(Intent(this, VoiceService::class.java))

        AndPermission.with(this)
            .runtime()
            .permission(Permission.RECORD_AUDIO)
            .onGranted { ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER) }
            .start()
    }

}