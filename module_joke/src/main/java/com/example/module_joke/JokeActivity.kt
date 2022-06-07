/**
 * @author: Zhang
 * @profile: 笑话
 */
package com.example.module_joke

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_JOKE)
class JokeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joke)
    }
}