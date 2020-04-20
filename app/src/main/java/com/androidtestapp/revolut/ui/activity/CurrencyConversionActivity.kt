package com.androidtestapp.revolut.ui.activity

import android.os.Bundle
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.androidtestapp.revolut.AppConstants.BASE_CURRENCY_POSITION
import com.androidtestapp.revolut.AppConstants.CURRENCY_AMOUNT_ZERO
import com.androidtestapp.revolut.AppConstants.DEFAULT_BASE_CURRENCY
import com.androidtestapp.revolut.AppConstants.EDIT_TEXT_DIGITS_AFTER_DECIMAL
import com.androidtestapp.revolut.AppConstants.EDIT_TEXT_DIGITS_BEFORE_DECIMAL
import com.androidtestapp.revolut.R
import com.androidtestapp.revolut.repository.remote_repository.dto.CurrencyConverter
import com.androidtestapp.revolut.ui.adapter.CurrencyConversionAdapter
import com.androidtestapp.revolut.ui.util.DecimalDigitsInputFilter
import com.androidtestapp.revolut.ui.util.NetworkUtility
import com.androidtestapp.revolut.ui.util.OnTextChangeListener
import com.androidtestapp.revolut.ui.viewmodel.CurrencyConversionViewModel
import com.bumptech.glide.Glide
import org.koin.android.viewmodel.ext.android.viewModel

class CurrencyConversionActivity : AppCompatActivity() {

    private val viewModel: CurrencyConversionViewModel by viewModel()

    private val baseCurrencyImageView: ImageView by lazy { findViewById<ImageView>(R.id.currencyFlagImageView) }
    private val baseCurrencyCodeTextView: TextView by lazy { findViewById<TextView>(R.id.currencyCodeTextView) }
    private val baseCurrencyNameTextView: TextView by lazy { findViewById<TextView>(R.id.currencyNameTextView) }
    private val baseCurrencyEditText: EditText by lazy { findViewById<EditText>(R.id.currencyAmountEditText) }

    private val currencyConverterRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.currencyConverterRecyclerView) }
    private val errorTextView: TextView by lazy { findViewById<TextView>(R.id.errorTextView) }

    private val progressBar: ContentLoadingProgressBar by lazy {
        findViewById<ContentLoadingProgressBar>(
            R.id.contentLoadingProgressBar
        )
    }

    private val baseCurrencyView: View by lazy { findViewById<View>(R.id.includeLayout) }

    private lateinit var currencyConversionAdapter: CurrencyConversionAdapter

    private var defaultBaseCurrencyCode = DEFAULT_BASE_CURRENCY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        if (savedInstanceState == null) {
            showProgressBar()
        }

        if (NetworkUtility.isNetworkAvailable(this@CurrencyConversionActivity) == null) {
            showErrorView()
        }

        registerTextChangeListener()

        setRecyclerViewAdapter()

        subscribeCurrencyUpdates()
    }

    private fun subscribeCurrencyUpdates() {

        viewModel.getCurrencyRates().observe(
            this@CurrencyConversionActivity,
            Observer { currencyConverterList ->

                if (!currencyConverterList.isNullOrEmpty()) {
                    showCurrencyList()

                    updateBaseCurrencyUI(currencyConverterList)

                    setCurrencyList(currencyConverterList)

                }
            })
    }

    private fun setCurrencyList(currencyConverterList: List<CurrencyConverter>) {
        val mutableCurrencyConverterList = currencyConverterList.let {
            it.toMutableList().drop(1)
        }

        currencyConversionAdapter.currencyConverterList = mutableCurrencyConverterList

    }

    private fun setRecyclerViewAdapter() {
        currencyConversionAdapter = CurrencyConversionAdapter(
            onCurrencyClickListener = { currencyCode, currencyAmount ->

                if (NetworkUtility.isNetworkAvailable(this@CurrencyConversionActivity) == null) {
                    showNetworkUnavailableToast()
                } else {
                    defaultBaseCurrencyCode = currencyCode
                    viewModel.updateCurrencyRates(currencyCode, currencyAmount)
                }
            }
        )

        // To maintain scroll state of recyclerview when adapter data is notified
        val recyclerViewState = currencyConverterRecyclerView.layoutManager?.onSaveInstanceState()
        currencyConverterRecyclerView.adapter = currencyConversionAdapter
        currencyConverterRecyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private fun registerTextChangeListener() {
        val inputFilter = arrayOf<InputFilter>(
            DecimalDigitsInputFilter(
                EDIT_TEXT_DIGITS_BEFORE_DECIMAL,
                EDIT_TEXT_DIGITS_AFTER_DECIMAL
            )
        )
        baseCurrencyEditText.setFilters(inputFilter)

        baseCurrencyEditText.addTextChangedListener(OnTextChangeListener { text ->
            if (NetworkUtility.isNetworkAvailable(this@CurrencyConversionActivity) == null) {
                runOnUiThread { showNetworkUnavailableToast() }
            } else {
                var currencyAmount = CURRENCY_AMOUNT_ZERO
                if (!text.isNullOrBlank()) {
                    currencyAmount = text.toDouble()
                }
                viewModel.updateCurrencyRates(defaultBaseCurrencyCode, currencyAmount)
            }
        })
    }

    private fun showNetworkUnavailableToast() {
        val toast = Toast.makeText(
            this@CurrencyConversionActivity,
            getString(R.string.network_unavailable_error_msg),
            Toast.LENGTH_SHORT
        )
        val view = toast?.view?.findViewById<TextView>(android.R.id.message)
        view?.let {
            view.gravity = Gravity.CENTER
        }
        toast.show()
    }

    private fun updateBaseCurrencyUI(currencyConverterList: List<CurrencyConverter>) {
        val currency = currencyConverterList[BASE_CURRENCY_POSITION]
        Glide.with(this@CurrencyConversionActivity)
            .load(currency.currencyFlag)
            .into(baseCurrencyImageView)
        baseCurrencyCodeTextView.text = currency.currencyCode
        baseCurrencyNameTextView.text = currency.currencyName

        if (currency.convertedAmount == CURRENCY_AMOUNT_ZERO) {
            baseCurrencyEditText.hint = CURRENCY_AMOUNT_ZERO.toString()
        } else {
            val amount = currency.convertedAmount
            baseCurrencyEditText.setText(amount.toString())
            baseCurrencyEditText.setSelection(amount.toString().length)
        }


    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        progressBar.show()
    }

    private fun showCurrencyList() {
        hideProgressBar()

        currencyConverterRecyclerView.visibility = View.VISIBLE
        baseCurrencyView.visibility = View.VISIBLE
        errorTextView.visibility = View.GONE
    }

    private fun hideProgressBar() {
        progressBar.hide()
        progressBar.visibility = View.GONE

    }

    private fun showErrorView() {
        hideProgressBar()
        errorTextView.visibility = View.VISIBLE
        baseCurrencyView.visibility = View.GONE
        currencyConverterRecyclerView.visibility = View.GONE
    }
}
