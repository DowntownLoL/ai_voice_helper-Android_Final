/**
 * @author: Zhang
 * @description: 说星座名返回时间
 */
package com.example.lib_base.helper.`fun`

import android.content.Context
import com.example.lib_base.R

object ConsTellHelper {

    private lateinit var mNameArray: Array<String>
    private lateinit var mTimeArray: Array<String>

    fun initHelper(mContext: Context) {
        mNameArray = mContext.resources.getStringArray(R.array.ConstellArray)
        mTimeArray = mContext.resources.getStringArray(R.array.ConstellTimeArray)
    }

    fun getConsTellTime(consTellName: String): String {
        mNameArray.forEachIndexed { index, s ->
            if (s == consTellName) {
                return mTimeArray[index]
            }
        }
        return "查询不到结果"
    }
}