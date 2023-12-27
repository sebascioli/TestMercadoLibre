package com.example.testmercadolibre.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.util.Locale
import kotlin.math.abs

object Utils {

    const val TAG = "sscioli"
    const val HTTP_TIMEOUT = 10L // s
    const val SPLASH_TIME = 1000 // ms
    const val URL_BASE = "https://api.mercadolibre.com"
    const val ID_SITE = "MLA"
    const val KEY_PRODUCT = "product"

    fun formatPrice(price: Float, currency: String?, decimals: Int): String {
        val space = if (currency == null || currency == "") "" else " "
        val priceFormat = "%,." + decimals + "f"
        val priceStr: String = if (price >= 0.0) {
            currency + space + String.format(Locale.getDefault(), priceFormat, price)
        } else {
            "-$currency$space" + String.format(Locale.getDefault(), priceFormat, abs(price))
        }
        return priceStr
    }

    fun showKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

}
