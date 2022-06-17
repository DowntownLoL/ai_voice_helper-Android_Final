/**
 * @author: Zhang
 * @description: 对外网络管理类
 */
package com.example.lib_network

import android.util.Log
import com.example.lib_network.bean.*
import com.example.lib_network.http.HttpKey
import com.example.lib_network.http.HttpUrl
import com.example.lib_network.http.HttpUrl.WEATHER_BASE_URL
import com.example.lib_network.impl.HttpImplService
import com.example.lib_network.interceptor.HttpInterceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object HttpManager {

    private const val PAGE_SIZE = 20

    const val JSON = "Content-type:application/json;charset=UTF-8" // for tuling robot

    //创建客户端
    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpInterceptor()).build()
    }

    // 天气对象
    private val retrofitWeather by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // 天气接口对象
    private val apiWeather by lazy {
        retrofitWeather.create(HttpImplService::class.java)
    }

    // 查询天气
    fun queryWeather(city: String, callback: Callback<WeatherData>) {
        apiWeather.getWeather(city, HttpKey.WEATHER_KEY).enqueue(callback)
    }

    // 笑话对象
    private val retrofitJoke by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.JOKE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 笑话接口对象
    private val apiJoke by lazy {
        retrofitJoke.create(HttpImplService::class.java)
    }

    // 查询笑话
    fun queryJoke(callback: Callback<JokeOneData>) {
        apiJoke.queryJoke(HttpKey.JOKE_KEY).enqueue(callback)
    }

    // 查询笑话列表
    fun queryJokeList(page: Int, callback: Callback<JokeListData>) {
        apiJoke.queryJokeList(HttpKey.JOKE_KEY, page, PAGE_SIZE).enqueue(callback)
    }

    // 星座对象
    private val retrofitConsTell by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.CONS_TELL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 星座接口对象
    private val apiConsTell by lazy {
        retrofitConsTell.create(HttpImplService::class.java)
    }

    // 查询今天星座详情
    fun queryTodayConsTellInfo(name: String, callback: Callback<TodayData>) {
        apiConsTell.queryTodayConsTellInfo(name, "today", HttpKey.CONS_TELL_KEY).enqueue(callback)
    }

    // 查询明天星座详情
    fun queryTomorrowConsTellInfo(name: String, callback: Callback<TodayData>) {
        apiConsTell.queryTodayConsTellInfo(name, "tomorrow", HttpKey.CONS_TELL_KEY).enqueue(callback)
    }

    // 查询本周星座详情
    fun queryWeekConsTellInfo(name: String, callback: Callback<WeekData>) {
        apiConsTell.queryWeekConsTellInfo(name, "week", HttpKey.CONS_TELL_KEY).enqueue(callback)
    }

    // 查询本月星座详情
    fun queryMonthConsTellInfo(name: String, callback: Callback<MonthData>) {
        apiConsTell.queryMonthConsTellInfo(name, "month", HttpKey.CONS_TELL_KEY).enqueue(callback)
    }

    // 查询今年星座详情
    fun queryYearConsTellInfo(name: String, callback: Callback<YearData>) {
        apiConsTell.queryYearConsTellInfo(name, "year", HttpKey.CONS_TELL_KEY).enqueue(callback)
    }

    // 机器人
    private val retrofitAiRobot by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.ROBOT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 机器人接口对象
    private val apiRobot by lazy {
        retrofitAiRobot.create(HttpImplService::class.java)
    }

    // 机器人对话 处理api返回的json
    fun aiRobotChat(text: String, callback: Callback<RobotData>) {
        val jsonObject = JSONObject()
        jsonObject.put("reqType", 0)

        val perception = JSONObject()

        val inputText = JSONObject()
        inputText.put("text", text)

        perception.put("inputText", inputText)

        val userInfo = JSONObject()
        userInfo.put("apiKey", HttpKey.ROBOT_KEY)
        userInfo.put("userId", "ai")

        jsonObject.put("perception", perception)
        jsonObject.put("userInfo", userInfo)

        Log.i("=============>", jsonObject.toString())

        val body = RequestBody.create(MediaType.parse(JSON), jsonObject.toString())

        apiRobot.aiRobot(body).enqueue(callback)
    }

}