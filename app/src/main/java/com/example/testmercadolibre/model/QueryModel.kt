package com.example.testmercadolibre.model

import com.google.gson.annotations.SerializedName

class QueryModel {

    @SerializedName("site_id") private var siteId: String? = null
    @SerializedName("query") private var query: String? = null
    @SerializedName("paging") private var paging: Paging? = null
    @SerializedName("results") private var products = ArrayList<Product>()
    @SerializedName("secondary_results") private var secondaryResults = ArrayList<Any>()
    @SerializedName("related_results") private var relatedResults = ArrayList<Any>()
    @SerializedName("sort") private var sort: Sort? = null
    @SerializedName("available_sorts") private var availableSorts = ArrayList<Any>()
    @SerializedName("filters") private var filters = ArrayList<Any>()
    @SerializedName("available_filters") private var availableFilters = ArrayList<Any>()

    fun getPaging() = this.paging
    fun getProducts() = this.products

}
