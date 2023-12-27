package com.example.testmercadolibre.controller

import com.example.testmercadolibre.model.QueryModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    // example: curl -X GET https://api.mercadolibre.com/sites/MLA/search?q=Motorola%20G6
    @GET("sites/{SITE_ID}/search")
    fun getItemsByQuery(@Path("SITE_ID") idSite: String?, @Query("q") query: String?): Call<QueryModel?>?

    @GET("sites/{SITE_ID}/search")
    fun getItemsByQuery(@Path("SITE_ID") idSite: String?, @Query("q") query: String?, @Query("offset") offset: String?): Call<QueryModel?>?

    @GET("sites/{SITE_ID}/search")
    fun getItemsByQuery(@Path("SITE_ID") idSite: String?, @Query("q") query: String?, @Query("offset") offset: Int, @Query("limit") limit: Int): Call<QueryModel?>?

    // example: curl -X GET https://api.mercadolibre.com/sites/MLA/search?category=MLA1055
    @GET("sites/{SITE_ID}/search?category={CATEGORY_ID}")
    fun getItemsByCategory(@Path("SITE_ID") idSite: String?, @Path("CATEGORY_ID") idCategory: String?): Call<ResponseBody?>?

}
