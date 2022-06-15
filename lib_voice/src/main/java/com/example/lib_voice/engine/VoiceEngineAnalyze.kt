/**
 *  @description: 语音引擎分析
 */
package com.example.lib_voice.engine

import android.util.Log
import com.example.lib_voice.impl.OnNluResultListener
import com.example.lib_voice.words.NluWrods
import org.json.JSONObject

object VoiceEngineAnalyze {

    private var TAG = VoiceEngineAnalyze::class.java.simpleName

    private lateinit var mOnNluResultListener: OnNluResultListener

    // 分析结果
    fun analyzeNlu(nlu: JSONObject, mOnNluResultListener: OnNluResultListener) {
        this.mOnNluResultListener = mOnNluResultListener
        // 用户说的话
        val rawText = nlu.optString("raw_text")
        Log.i(TAG, "rawText:$rawText")

        // 解析results
        val results = nlu.optJSONArray("results") ?: return
        val nluResultLength = results.length()
        when {
            nluResultLength <= 0 -> return
            // 单条命中
            results.length() >= 1 -> analyzeNluSingle(results[0] as JSONObject)
            else -> {
                // 多条命中
            }
        }
    }

    // 处理单条结果
    private fun analyzeNluSingle(result: JSONObject) {
        val domain = result.optString("domain")
        val intent = result.optString("intent")
        val slots = result.optJSONObject("slots")

        when (domain) {
            NluWrods.NLU_WEATHER -> {
                // 获取其他类型

            }
        }
    }
}