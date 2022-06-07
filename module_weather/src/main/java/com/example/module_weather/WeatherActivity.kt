/**
 * @author: Zhang
 * @profile: 天气
 */
package com.example.module_weather

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_WEATHER)
class WeatherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
    }
}