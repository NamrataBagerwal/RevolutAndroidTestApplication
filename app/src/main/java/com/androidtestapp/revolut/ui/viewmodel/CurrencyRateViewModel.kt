package com.androidtestapp.revolut.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidtestapp.revolut.AppConstants.DEFAULT_BASE_CURRENCY
import com.androidtestapp.revolut.AppConstants.DEFAULT_BASE_CURRENCY_AMOUNT
import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remote_repository.dto.CurrencyConverter
import com.androidtestapp.revolut.repository.remote_repository.dto.CurrencyEnum
import com.androidtestapp.revolut.repository.remote_repository.webservice.entity.CurrencyConversionRates
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CurrencyRateViewModel(private val repository: Repository<CurrencyConversionRates>) : ViewModel() {

    private val parentJob = SupervisorJob()

    private val coroutineContextDispatcherIO: CoroutineContext
        get() = parentJob + viewModelScope.coroutineContext + Dispatchers.IO

    private val coroutineContextDispatcherDefault: CoroutineContext
        get() = parentJob + viewModelScope.coroutineContext + Dispatchers.Default

    private val coroutineContextDedicatedThread: CoroutineContext
        get() = newSingleThreadContext("CurrencyApiCall")

    private val currencyRatesLiveData: MutableLiveData<List<CurrencyConverter>> by lazy {
        MutableLiveData<List<CurrencyConverter>>().also {
            startUpdatingCurrencyRates(DEFAULT_BASE_CURRENCY, DEFAULT_BASE_CURRENCY_AMOUNT)
        }
    }

    fun getCurrencyRates(): LiveData<List<CurrencyConverter>>{
        return currencyRatesLiveData
    }

    fun updateCurrencyRates(baseCurrency: String, baseCurrencyAmount: Double) {
//        stopUpdatingCurrencyRates()
        startUpdatingCurrencyRates(baseCurrency, baseCurrencyAmount)

    }

    private fun startUpdatingCurrencyRates(baseCurrency: String, baseCurrencyAmount: Double): Job =
        viewModelScope.launch(context = coroutineContextDedicatedThread) {
            while (isActive) {
                updateCurrencyRatesEverySecond(baseCurrency, baseCurrencyAmount)
                Thread.sleep(1000)
            }

        }

    private fun stopUpdatingCurrencyRates() {
//        if (coroutineContextDispatcherIO.isActive) {
//            coroutineContextDispatcherIO.cancelChildren()
//        }
//        if (coroutineContextDispatcherDefault.isActive) {
//            coroutineContextDispatcherDefault.cancelChildren()
//        }

        if(coroutineContextDedicatedThread.isActive){
            coroutineContextDedicatedThread.cancelChildren()
        }
    }

    private suspend fun updateCurrencyRatesEverySecond(
        baseCurrency: String,
        baseCurrencyAmount: Double
    ) {
        Log.d("TAG", "  updateCurrencyRatesEverySecond")
        val currencyRates: CurrencyConversionRates? = repository.invokeWebService(baseCurrency)
        if (currencyRates != null) {
            val currencyConverterList =
                convertResponseToCurrencyConverter(currencyRates, baseCurrencyAmount)
            currencyRatesLiveData.postValue(
                currencyConverterList
            )
        }
    }

    private fun convertResponseToCurrencyConverter(
        currencyRates: CurrencyConversionRates,
        baseCurrencyAmount: Double
    ): List<CurrencyConverter> {

        val currencyConverterList: MutableList<CurrencyConverter> = mutableListOf()
//        viewModelScope.launch(context = coroutineContextDispatcherDefault) {

            val baseCurrencyRate: String = currencyRates.baseCurrency
            var currency: CurrencyEnum = CurrencyEnum.getCurrencyByCode(baseCurrencyRate)
            var currencyConverter = CurrencyConverter(
                currency.getCurrencyFlag(),
                currency.currencyCode,
                currency.currencyName,
                baseCurrencyAmount
            )
            currencyConverterList.add(currencyConverter)

            val rates: LinkedHashMap<String, Double> = currencyRates.rates
            for (key in rates.keys) {
                currency = CurrencyEnum.getCurrencyByCode(key)
                currencyConverter = CurrencyConverter(
                    currency.getCurrencyFlag(),
                    currency.currencyCode,
                    currency.currencyName,
                    baseCurrencyAmount * rates[key]!!
                )
                currencyConverterList.add(currencyConverter)
            }
//        }
        return currencyConverterList

    }

    override fun onCleared() {
        super.onCleared()
//        if (coroutineContextDispatcherIO.isActive) {
//            coroutineContextDispatcherIO.cancelChildren()
//        }
//        if (coroutineContextDispatcherDefault.isActive) {
//            coroutineContextDispatcherDefault.cancelChildren()
//        }

        stopUpdatingCurrencyRates()

        if (parentJob.isActive) {
            parentJob.cancel()
        }
    }
}

