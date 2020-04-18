package com.androidtestapp.revolut.ui.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.androidtestapp.revolut.AppConstants.BASE_CURRENCY_POSITION
import com.androidtestapp.revolut.AppConstants.CURRENCY_AMOUNT_ZERO
import com.androidtestapp.revolut.AppConstants.DEFAULT_BASE_CURRENCY
import com.androidtestapp.revolut.R
import com.androidtestapp.revolut.repository.remote_repository.dto.CurrencyConverter
import com.androidtestapp.revolut.ui.adapter.CurrencyConverterAdapter
import com.androidtestapp.revolut.ui.util.NetworkUtility
import com.androidtestapp.revolut.ui.util.OnTextChangeListener
import com.androidtestapp.revolut.ui.viewmodel.CurrencyRateViewModel
import com.bumptech.glide.Glide
import org.koin.android.viewmodel.ext.android.viewModel

class CurrencyConverterActivity : AppCompatActivity() {

    private val viewModel: CurrencyRateViewModel by viewModel()

    private val baseCurrencyImageView: ImageView by lazy { findViewById<ImageView>(R.id.currencyFlagImageView) }
    private val baseCurrencyCodeTextView: TextView by lazy { findViewById<TextView>(R.id.currencyCodeTextView) }
    private val baseCurrencyNameTextView: TextView by lazy { findViewById<TextView>(R.id.currencyNameTextView) }
    private val baseCurrencyEditText: EditText by lazy { findViewById<EditText>(R.id.currencyAmountEditText) }

    private val currencyConverterRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.currencyConverterRecyclerView) }

    private val progressBar: ContentLoadingProgressBar by lazy {
        findViewById<ContentLoadingProgressBar>(
            R.id.contentLoadingProgressBar
        )
    }

    private val baseCurrencyView: View by lazy { findViewById<View>(R.id.includeLayout) }

    private var defaultBaseCurrencyCode = DEFAULT_BASE_CURRENCY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        if (savedInstanceState == null) {
            showProgressBar()
        }

        subscribeCurrencyUpdates()
    }

    private fun subscribeCurrencyUpdates() {

//        if(NetworkUtility.isNetworkAvailable(this@CurrencyConverterActivity) != null){
            viewModel.getCurrencyRates().observe(
                this@CurrencyConverterActivity,
                Observer { currencyConverterList ->

                    if (!currencyConverterList.isNullOrEmpty()) {
                        showCurrencyList()

                        updateBaseCurrencyUI(currencyConverterList)

                        registerTextChangeListener(defaultBaseCurrencyCode)

                        setCurrencyList(currencyConverterList)

                    }
                })
//        }
    }

    private fun setCurrencyList(currencyConverterList: List<CurrencyConverter>) {
        val mutableCurrencyConverterList = currencyConverterList.let {
            it.toMutableList().drop(1)
        }

        val currencyConverterAdapter = CurrencyConverterAdapter(
            onCurrencyClickListener = { currencyCode, currencyAmount ->
                defaultBaseCurrencyCode = currencyCode
                viewModel.updateCurrencyRates(currencyCode, currencyAmount)
            },
            onCurrencyAmountChangeListener = { currencyAmount ->


            }
        )

        // To maintain scroll state of recyclerview when adapter data is notified
        val recyclerViewState = currencyConverterRecyclerView.layoutManager?.onSaveInstanceState()
        currencyConverterRecyclerView.adapter = currencyConverterAdapter
        currencyConverterAdapter.currencyConverterList = mutableCurrencyConverterList
        currencyConverterRecyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private fun registerTextChangeListener(defaultBaseCurrencyCode: String) {
        baseCurrencyEditText.addTextChangedListener(OnTextChangeListener { text ->
            var currencyAmount = CURRENCY_AMOUNT_ZERO
            if (!text.isNullOrBlank()) {
                currencyAmount = text.toDouble()
            }
            viewModel.updateCurrencyRates(defaultBaseCurrencyCode, currencyAmount)
        })
    }

    private fun updateBaseCurrencyUI(currencyConverterList: List<CurrencyConverter>) {
        val currency = currencyConverterList[BASE_CURRENCY_POSITION]
//        if (defaultBaseCurrencyCode == currency.currencyCode) {
            Glide.with(this@CurrencyConverterActivity)
                .load(currency.currencyFlag)
                .into(baseCurrencyImageView)
            baseCurrencyCodeTextView.text = currency.currencyCode
            baseCurrencyNameTextView.text = currency.currencyName

            if (currency.convertedAmount == CURRENCY_AMOUNT_ZERO) {
                baseCurrencyEditText.hint = CURRENCY_AMOUNT_ZERO.toString()
            } else {
                baseCurrencyEditText.setText(currency.convertedAmount.toString())
                baseCurrencyEditText.setSelection(currency.convertedAmount.toString().length)
            }

//        }

    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        progressBar.show()
    }

    private fun showCurrencyList() {
        progressBar.hide()
        progressBar.visibility = View.GONE

        currencyConverterRecyclerView.visibility = View.VISIBLE
        baseCurrencyView.visibility = View.VISIBLE
    }
}
