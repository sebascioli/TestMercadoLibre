package com.example.testmercadolibre.model

import com.google.gson.annotations.SerializedName

class Paging {

    @SerializedName("total") private var total = 0
    @SerializedName("offset") private var offset = 0
    @SerializedName("limit") private var limit = 0
    @SerializedName("primary_results") private var primaryResults = 0

    fun getTotal(): Int = this.total

}
