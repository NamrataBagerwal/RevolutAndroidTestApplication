package com.androidtestapp.revolut.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidtestapp.revolut.AppConstants.DEFAULT_BASE_CURRENCY
import com.androidtestapp.revolut.AppConstants.DEFAULT_BASE_CURRENCY_AMOUNT
import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remote_repository.RemoteRepositoryImpl
import com.androidtestapp.revolut.repository.remote_repository.dto.CurrencyConverter
import com.androidtestapp.revolut.repository.remote_repository.dto.CurrencyEnum
import com.androidtestapp.revolut.repository.remote_repository.webservice.CurrencyRepositoryImpl
import com.androidtestapp.revolut.repository.remote_repository.webservice.entity.CurrencyConversionRates
import kotlinx.coroutines.*

class CurrencyRateViewModel(private val repository: Repository<CurrencyConversionRates>) : ViewModel() {

    private val currentScope: CoroutineScope = viewModelScope

    private val currencyRatesLiveData: MutableLiveData<List<CurrencyConverter>> by lazy {
        MutableLiveData<List<CurrencyConverter>>().also {
            startUpdatingCurrencyRates(DEFAULT_BASE_CURRENCY, DEFAULT_BASE_CURRENCY_AMOUNT)
        }
    }

    fun getCurrencyRates(): LiveData<List<CurrencyConverter>>{
        return currencyRatesLiveData
    }

//    init {
//        startUpdatingCurrencyRates("GBP", 1.0)
//    }

    fun updateCurrencyRates(baseCurrency: String, baseCurrencyAmount: Double) {
        stopUpdatingCurrencyRates()
        startUpdatingCurrencyRates(baseCurrency, baseCurrencyAmount)

    }

    private fun startUpdatingCurrencyRates(baseCurrency: String, baseCurrencyAmount: Double): Job =
        currentScope.launch(Dispatchers.IO) {
            //        stopUpdatingCurrencyRates()

            while (isActive) {
                updateCurrencyRatesEverySecond(baseCurrency, baseCurrencyAmount)
                delay(1000)
            }

        }

    private fun stopUpdatingCurrencyRates() {
        if (currentScope.coroutineContext.isActive) {
            currentScope.coroutineContext.cancelChildren()
        }
    }

    private suspend fun updateCurrencyRatesEverySecond(
        baseCurrency: String,
        baseCurrencyAmount: Double
    ) {
        val currencyRates: CurrencyConversionRates? =
            repository.invokeWebService(baseCurrency)
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
        currentScope.launch(Dispatchers.Default) {

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
        }
        return currencyConverterList

    }

}

