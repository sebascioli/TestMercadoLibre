package com.example.testmercadolibre.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testmercadolibre.R

open class BaseActivity: AppCompatActivity() {
    private var loading: Dialog? = null

    fun showLoading(message: String? = null) {
        if (this.loading == null) {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null, false)
            val txtDescription = dialogView.findViewById<TextView>(R.id.infoLoading)
            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)
            runOnUiThread{
                this.loading = builder.create()
                this.loading?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                callShowLoading(message, txtDescription)
            }
        } else {
            callShowLoading(message)
        }
    }

    fun hideLoading() = this.loading?.dismiss()

    private fun callShowLoading(message: String? = null, textView: TextView? = null) {
        runOnUiThread {
            val txtTitle = textView ?: this.loading?.findViewById<TextView>(R.id.infoLoading)
            if (message != null){
                txtTitle?.text = message
                txtTitle?.visibility = View.VISIBLE
            } else {
                txtTitle?.text = null
                txtTitle?.visibility = View.GONE
            }
            if (this.loading?.isShowing == true) {
                hideLoading()
            }
            loading?.show()
        }
    }

}
