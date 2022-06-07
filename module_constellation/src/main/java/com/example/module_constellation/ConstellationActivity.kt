/**
 * @author: Zhang
 * @profile: 星座
 */
package com.example.module_constellation

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_CONSTELLATION)
class ConstellationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constellation)
    }

}