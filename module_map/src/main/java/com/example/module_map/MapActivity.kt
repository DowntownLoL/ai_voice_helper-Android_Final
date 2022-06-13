/**
 * @author: Zhang
 * @description: 地图
 */
package com.example.module_map

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_MAP)
class MapActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_map
    }

    override fun getTitleText(): String {
        return getString(com.example.lib_base.R.string.app_title_map)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {

    }

}