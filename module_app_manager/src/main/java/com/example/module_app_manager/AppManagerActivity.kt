/**
 * @author: Zhang
 * @description: 应用管理App
 */
package com.example.module_app_manager

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_APP_MANAGER)
class AppManagerActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_app_manager
    }

    override fun getTitleText(): String {
        return getString(com.example.lib_base.R.string.app_title_app_manager)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {

    }

}