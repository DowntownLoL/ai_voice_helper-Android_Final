package com.example.lib_network.impl

import com.example.lib_network.http.HttpUrl
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HttpImplService {


    @GET(HttpUrl.WEATHER_ACTION)
    fun getWeather(@Query("city") city: String, @Query("key") key: String): Call<ResponseBody>
}