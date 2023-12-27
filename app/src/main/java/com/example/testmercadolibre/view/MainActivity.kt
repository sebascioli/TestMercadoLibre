package com.example.testmercadolibre.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testmercadolibre.R
import com.example.testmercadolibre.controller.ApiInterface
import com.example.testmercadolibre.controller.RestService
import com.example.testmercadolibre.databinding.ActivityMainBinding
import com.example.testmercadolibre.model.Product
import com.example.testmercadolibre.model.QueryModel
import com.example.testmercadolibre.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class MainActivity: BaseActivity(), ProductListAdapter.OnProductListener {

    private lateinit var binding: ActivityMainBinding
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: ProductListAdapter? = null
    private var apiInterface: ApiInterface? = null
    private var query: String? = null
    private var isLoading = true
    private var offset = 0
    private var limit: Int = 50
    private var totalFound: Int = -1
    private var pastVisibleItems = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var previousTotal: Int = 0
    private val viewThreshold = 0
    //val DRAWABLE_LEFT = 0
    //val DRAWABLE_TOP = 1
    val DRAWABLE_RIGHT = 2
    //val DRAWABLE_BOTTOM = 3

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        // (sscioli - 2023-12-26) Ocultar action bar
        supportActionBar?.hide()

        // (sscioli - 2023-12-26) Deshabilitar night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        layoutManager = LinearLayoutManager(this)

        this.binding.resultsView.visibility = View.GONE
        this.binding.emptyView.visibility = View.VISIBLE

        this.binding.productsRecycler.setHasFixedSize(true)
        this.binding.productsRecycler.layoutManager = layoutManager
        this.binding.productsRecycler.addItemDecoration(DividerItemDecoration(this.binding.productsRecycler.context, DividerItemDecoration.VERTICAL))

        this.binding.productsSwipe.setOnRefreshListener {
            this.binding.productsSwipe.isRefreshing = false
            clearScreen()
            getQueryResponse()
        }

        this.binding.queryInput.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action === KeyEvent.ACTION_DOWN && keyCode === KeyEvent.KEYCODE_ENTER) {
                Utils.hideKeyboard(this@MainActivity)
                val queryNew: String = binding.queryInput.text.toString()
                if (queryNew.isNotEmpty() && queryNew != query) {
                    binding.emptyView.visibility = View.GONE
                    binding.resultsView.visibility = View.VISIBLE
                    query = queryNew
                    clearScreen()
                    binding.infoItemsTV.text = getString(R.string.searching)
                    getQueryResponse()
                }
                return@setOnKeyListener true
            }
            false
        }

        this.binding.queryInput.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action === MotionEvent.ACTION_UP) {
                if (motionEvent.rawX >= binding.queryInput.right - binding.queryInput.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    binding.queryInput.text?.clear()
                    query = ""
                    clearScreen()
                    binding.resultsView.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                    binding.queryInput.requestFocus()
                    Utils.showKeyboard(this@MainActivity)
                    return@setOnTouchListener true
                }
            }
            false
        }

        this.binding.queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val right = binding.queryInput.compoundDrawables[DRAWABLE_RIGHT]
                right?.setVisible(charSequence.isNotEmpty(), false)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        this.binding.productsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = layoutManager?.childCount ?: 0
                totalItemCount = layoutManager?.itemCount ?: 0
                pastVisibleItems = layoutManager?.findFirstVisibleItemPosition() ?: 0
                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false
                            previousTotal = totalItemCount
                        }
                    } else if (totalItemCount - visibleItemCount <= pastVisibleItems + viewThreshold) {
                        Log.d(Utils.TAG, "Carga 50 más...")
                        offset += limit
                        getQueryResponse()
                        isLoading = true
                    }
                }
            }
        })

    }

    private fun clearScreen() {
        this.offset = 0
        this.previousTotal = 0
        this.totalFound = -1
        this.binding.infoItemsTV.text = getString(R.string.empty_search_results)
        if (this.adapter != null) this.adapter?.clearData()
    }

    private fun getQueryResponse() {
        //this.binding.progressBar.setVisibility(View.VISIBLE)
        showLoading(message = if (offset > 0) "Cargando más resultados..." else null)
        if (this.apiInterface == null) this.apiInterface = RestService(Utils.URL_BASE).getApiInterface()
        val call: Call<QueryModel?>? = this.apiInterface?.getItemsByQuery(Utils.ID_SITE, this.query, this.offset, this.limit)
        call?.enqueue(object : Callback<QueryModel?> {
            override fun onResponse(call: Call<QueryModel?>, response: Response<QueryModel?>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    call.cancel()
                    val body: QueryModel? = response.body()
                    if (body != null) {
                        if (totalFound == -1) {
                            totalFound = body.getPaging()?.getTotal() ?: 0
                            binding.infoItemsTV.text = resources.getQuantityString(R.plurals.results_found, totalFound, totalFound)
                        }
                        val products: ArrayList<Product> = body.getProducts()
                        if (adapter == null) {
                            adapter = ProductListAdapter(this@MainActivity, products, this@MainActivity)
                            binding.productsRecycler.adapter = adapter
                        } else adapter?.addProducts(products)
                    }
                }
                //binding.progressBar.setVisibility(View.GONE)
                hideLoading()
            }

            override fun onFailure(call: Call<QueryModel?>, t: Throwable) {
                Toast.makeText(this@MainActivity, getString(R.string.search_failed), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onProductClick(product: Product?) {
        val intent = Intent(this, ProductViewerActivity::class.java)
        intent.putExtra(Utils.KEY_PRODUCT, product)
        startActivity(intent)
    }

}
