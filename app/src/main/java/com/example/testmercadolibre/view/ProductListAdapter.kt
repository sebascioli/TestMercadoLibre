package com.example.testmercadolibre.view

import android.R.attr.thumb
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.testmercadolibre.R
import com.example.testmercadolibre.model.Product
import com.example.testmercadolibre.utils.CircleTransform
import com.example.testmercadolibre.utils.Utils
import com.google.gson.internal.LinkedTreeMap
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt


class ProductListAdapter(
    private var context: Context?,
    private var products: ArrayList<Product>?,
    private var onProductListener: OnProductListener?
): RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    interface OnProductListener {
        fun onProductClick(product: Product?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_product, parent, false)
        return ViewHolder(view, this.onProductListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = this.products?.get(position)

        holder.normalDeliveryCV.visibility = View.GONE
        try {
            if (product?.getTags()?.get(5).equals("shipping_guaranteed")) {
                holder.normalDeliveryCV.visibility = View.VISIBLE
            }
        } catch (_: Exception) { }

        holder.titleTV.text = product?.getTitle()

        val price: Float = product?.getPrice() ?: 0f
        holder.priceTV.text = Utils.formatPrice(price, "$", 0)

        holder.percentTV.visibility = View.GONE
        val originalPrice: Float = product?.getOriginalPrice() ?: 0f
        if (originalPrice > 0f) {
            val percent = ((originalPrice - price) / originalPrice * 100).roundToInt()
            holder.percentTV.visibility = View.VISIBLE
            holder.percentTV.text = "$percent% OFF"
        }

        holder.infoSellerTV.visibility = View.GONE
        try {
            val sellerArray = (product?.getSeller() as LinkedTreeMap<*, *>).values.toTypedArray()[1] as String
            val split = sellerArray.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val seller = split[split.size - 1]
            holder.infoSellerTV.text = this.context?.getString(R.string.sold_by) + " " + seller
        } catch (_: Exception) { }

        val thumbnail: String? = product?.getThumbnail()
        Picasso.get()
            .load(thumbnail?.replace("http:", "https:"))
            .centerCrop()
            .transform(CircleTransform(30, 0))
            .fit()
            .into(holder.thumbnailIV)

        holder.itemView.setOnClickListener {
            this.onProductListener?.onProductClick(product)
        }
    }

    override fun getItemCount(): Int = this.products?.size ?: 0

    fun addProducts(productsToAdd: ArrayList<Product>) {
        this.products?.addAll(productsToAdd)
        notifyDataSetChanged()
    }

    fun clearData() {
        this.products?.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, onProductListener: OnProductListener?): RecyclerView.ViewHolder(itemView) {
        val thumbnailIV: ImageView
        val normalDeliveryCV: CardView
        val titleTV: TextView
        val priceTV: TextView
        val percentTV: TextView
        val infoSellerTV: TextView
        private val onProductListener: OnProductListener?

        init {
            this.thumbnailIV = itemView.findViewById<ImageView>(R.id.thumbnailIV)
            this.normalDeliveryCV = itemView.findViewById<CardView>(R.id.normalDeliveryCV)
            this.titleTV = itemView.findViewById<TextView>(R.id.titleTV)
            this.priceTV = itemView.findViewById<TextView>(R.id.priceTV)
            this.percentTV = itemView.findViewById<TextView>(R.id.percentTV)
            this.infoSellerTV = itemView.findViewById<TextView>(R.id.infoSellerTV)
            this.onProductListener = onProductListener
        }

    }

}
