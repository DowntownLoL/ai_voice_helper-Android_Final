/**
 * @author: Zhang
 * @description: 日志
 */

package com.example.lib_base.utils

import android.util.Log
import com.example.lib_base.BuildConfig

object L {

    private const val TAG:String = "AiVoiceHelperApp"

    fun i(text: String?) {
        if(BuildConfig.DEBUG) {
            text?.let {
                Log.i(TAG, it)
            }
        }
    }

    fun e(text: String?) {
        if(BuildConfig.DEBUG) {
            text?.let {
                Log.e(TAG, it)
            }
        }
    }
}