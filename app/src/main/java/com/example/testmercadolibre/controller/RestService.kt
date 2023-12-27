package com.example.testmercadolibre.controller

import com.example.testmercadolibre.utils.Utils
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestService {

    private var apiInterface: ApiInterface? = null

    constructor(baseUrl: String) {
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(Utils.HTTP_TIMEOUT, TimeUnit.SECONDS)
        httpClient.readTimeout(Utils.HTTP_TIMEOUT, TimeUnit.SECONDS)
        httpClient.writeTimeout(Utils.HTTP_TIMEOUT, TimeUnit.SECONDS)
        val client = httpClient.build()
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl) // .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(GsonConverterFactory.create()) // .client(client)
            .build()
        this.apiInterface = retrofit.create(ApiInterface::class.java)
    }

    fun getApiInterface(): ApiInterface? = this.apiInterface

    fun setRestAPI(apiInterface: ApiInterface?) {
        this.apiInterface = apiInterface
    }

}
