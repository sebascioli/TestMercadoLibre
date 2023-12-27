package com.example.testmercadolibre.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Product(): Parcelable {

    @SerializedName("id") private var id: String? = null
    @SerializedName("site_id") private var siteId: String? = null
    @SerializedName("title") private var title: String? = null
    @SerializedName("seller") private val seller: Any? = null
    @SerializedName("price") private var price = 0f
    @SerializedName("currency_id") private var currencyId: String? = null
    @SerializedName("available_quantity") private var availableQuantity = 0
    @SerializedName("sold_quantity") private var soldQuantity = 0
    @SerializedName("buying_mode") private var buyingMode: String? = null
    @SerializedName("listing_type_id") private var listingTypeId: String? = null
    @SerializedName("stop_time") private var stopTime: String? = null
    @SerializedName("condition") private var condition: String? = null
    @SerializedName("permalink") private var permalink: String? = null
    @SerializedName("thumbnail") private var thumbnail: String? = null
    @SerializedName("accepts_mercadopago") private var acceptsMercadoPago = false
    @SerializedName("installments") private val installments: Any? = null
    @SerializedName("address") private val address: Any? = null
    @SerializedName("shipping") private val shipping: Any? = null
    @SerializedName("seller_address") private val sellerAddress: Any? = null
    @SerializedName("attributes") private val attributes: List<Any?>? = null
    @SerializedName("original_price") private var originalPrice = 0f
    @SerializedName("category_id") private var categoryId: String? = null
    @SerializedName("official_store_id") private var officialStoreId = 0
    @SerializedName("domain_id") private var domainId: String? = null
    @SerializedName("catalog_product_id") private var catalogProductId: String? = null
    @SerializedName("tags") private var tags: List<String?>? = null

    constructor(parcel: Parcel): this() {
        this.id = parcel.readString()
        this.siteId = parcel.readString()
        this.title = parcel.readString()
        this.price = parcel.readFloat()
        this.currencyId = parcel.readString()
        this.availableQuantity = parcel.readInt()
        this.soldQuantity = parcel.readInt()
        this.buyingMode = parcel.readString()
        this.listingTypeId = parcel.readString()
        this.stopTime = parcel.readString()
        this.condition = parcel.readString()
        this.permalink = parcel.readString()
        this.thumbnail = parcel.readString()
        this.acceptsMercadoPago = parcel.readByte() != 0.toByte()
        this.originalPrice = parcel.readFloat()
        this.categoryId = parcel.readString()
        this.officialStoreId = parcel.readInt()
        this.domainId = parcel.readString()
        this.catalogProductId = parcel.readString()
        this.tags = parcel.createStringArrayList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.id)
        parcel.writeString(this.siteId)
        parcel.writeString(this.title)
        parcel.writeFloat(this.price)
        parcel.writeString(this.currencyId)
        parcel.writeInt(this.availableQuantity)
        parcel.writeInt(this.soldQuantity)
        parcel.writeString(this.buyingMode)
        parcel.writeString(this.listingTypeId)
        parcel.writeString(this.stopTime)
        parcel.writeString(this.condition)
        parcel.writeString(this.permalink)
        parcel.writeString(this.thumbnail)
        parcel.writeByte(if (this.acceptsMercadoPago) 1 else 0)
        parcel.writeFloat(this.originalPrice)
        parcel.writeString(this.categoryId)
        parcel.writeInt(this.officialStoreId)
        parcel.writeString(this.domainId)
        parcel.writeString(this.catalogProductId)
        parcel.writeStringList(this.tags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product = Product(parcel)
        override fun newArray(size: Int): Array<Product?> = arrayOfNulls(size)
    }

    fun getTitle() = this.title
    fun getSeller() = this.seller
    fun getPrice() = this.price
    fun getCondition() = this.condition
    fun getThumbnail() = this.thumbnail
    fun getOriginalPrice() = this.originalPrice
    fun getTags() = this.tags

}
