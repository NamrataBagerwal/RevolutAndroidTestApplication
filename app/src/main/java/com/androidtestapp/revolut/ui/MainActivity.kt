package com.androidtestapp.revolut.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.androidtestapp.revolut.R
import com.androidtestapp.revolut.repository.remotedatastore.dto.CurrencyConverter
import com.androidtestapp.revolut.ui.adapter.CurrencyConverterAdapter
import com.androidtestapp.revolut.ui.util.OnTextChangeListener
import com.androidtestapp.revolut.ui.viewmodel.CurrencyRateViewModel
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private val viewModel: CurrencyRateViewModel by viewModels()

    private val baseCurrencyImageView: ImageView by lazy{ findViewById<ImageView>(R.id.currencyFlagImageView) }
    private val baseCurrencyCodeTextView: TextView by lazy{ findViewById<TextView>(R.id.currencyCodeTextView) }
    private val baseCurrencyNameTextView: TextView by lazy{ findViewById<TextView>(R.id.currencyNameTextView) }
    private val baseCurrencyEditText: EditText by lazy{ findViewById<EditText>(R.id.currencyAmountEditText) }

    private val currencyConverterRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.currencyConverterRecyclerView) }

    companion object{
        private const val DEFAULT_BASE_CURRENCY = "GBP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var defaultBaseCurrencyCode = DEFAULT_BASE_CURRENCY

        viewModel.currencyRatesLiveData.observe(this, Observer {
            currencyConverterList ->

            if(currencyConverterList.isNotEmpty()){
                val currency: CurrencyConverter = currencyConverterList[0]
                Glide.with(this)
                    .load(currency.currencyFlag)
                    .into(baseCurrencyImageView)
                baseCurrencyCodeTextView.text = currency.currencyCode
                baseCurrencyNameTextView.text = currency.currencyName

                baseCurrencyEditText.isClickable = true
                baseCurrencyEditText.setText(currency.convertedAmount.toString())
                baseCurrencyEditText.addTextChangedListener(OnTextChangeListener{
                                        text ->  viewModel.startUpdatingCurrencyRates(defaultBaseCurrencyCode, text.toDouble())
                })

                val mutableCurrencyConverterList = currencyConverterList.let {
                    it.toMutableList().drop(1)
                }

                val currencyConverterAdapter = CurrencyConverterAdapter{
                        currencyCode ->
                    run {
                        defaultBaseCurrencyCode = currencyCode
                        viewModel.startUpdatingCurrencyRates(currencyCode, 1.0)
                    }
                }
                currencyConverterRecyclerView.adapter = currencyConverterAdapter
                currencyConverterAdapter.currencyConverterList = mutableCurrencyConverterList
            }
        })
    }
}
