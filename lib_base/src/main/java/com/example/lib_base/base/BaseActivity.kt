/**
 * @author: Zhang
 * @description: None
 */
package com.example.lib_base.base

import android.os.Build
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    // 获取布局ID
    abstract fun getLayoutId(): Int

    // 获取标题
    abstract fun getTitleText(): String

    // 是否显示返回键
    abstract fun isShowBack(): Boolean

    // 初始化
    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportActionBar?.let {
                it.title = getTitleText() // title
                it.setDisplayHomeAsUpEnabled(isShowBack()) // 返回键
                it.elevation = 0f // 透明值
            }
        }

        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return true
    }
}