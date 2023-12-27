package com.example.testmercadolibre.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.testmercadolibre.databinding.ActivityProductViewerBinding
import com.example.testmercadolibre.model.Product
import com.example.testmercadolibre.utils.Utils
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class ProductViewerActivity: BaseActivity() {

    private var product: Product? = null
    private var binding: ActivityProductViewerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityProductViewerBinding.inflate(layoutInflater)
        setContentView(this.binding?.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Producto"
        supportActionBar?.subtitle = "Visualización de ejemplo"
        val intent = intent
        if (intent != null && intent.hasExtra(Utils.KEY_PRODUCT)) this.product = intent.getParcelableExtra(Utils.KEY_PRODUCT)
        setProductView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProductView() {
        if (this.product != null) {
            Picasso.get()
                .load(this.product?.getThumbnail()?.replace("http:", "https:"))
                .into(this.binding?.thumbnailIV)

            if (this.product?.getCondition() == "new") this.binding?.conditionTV?.text = "Nuevo"
            else this.binding?.conditionTV?.visibility = View.GONE

            this.binding?.titleTV?.text = this.product?.getTitle()

            this.binding?.originalPriceTV?.visibility = View.GONE
            this.binding?.percentTV?.visibility = View.GONE
            if ((this.product?.getOriginalPrice() ?: 0f) > 0f) {
                this.binding?.originalPriceTV?.visibility = View.VISIBLE
                this.binding?.percentTV?.visibility = View.VISIBLE
                this.binding?.originalPriceTV?.text = Utils.formatPrice(this.product?.getOriginalPrice() ?: 0f, "$", 0)
                val percent = (((this.product?.getOriginalPrice() ?: 0f) - (this.product?.getPrice() ?: 0f)) / (this.product?.getOriginalPrice() ?: 1f) * 100).roundToInt()
                this.binding?.percentTV?.text = "$percent% OFF"
            }

            this.binding?.priceTV?.text = Utils.formatPrice(this.product?.getPrice() ?: 0f, "$", 0)

        }
    }

}
