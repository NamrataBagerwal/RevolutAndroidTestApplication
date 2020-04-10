package com.androidtestapp.revolut.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remotedatastore.RemoteRepositoryImpl
import com.androidtestapp.revolut.repository.remotedatastore.dto.CurrencyConverter
import com.androidtestapp.revolut.repository.remotedatastore.dto.CurrencyEnum
import com.androidtestapp.revolut.repository.remotedatastore.entity.CurrencyConversionRates
import kotlinx.coroutines.*

class CurrencyRateViewModel() : ViewModel() {

    private val currentScope: CoroutineScope = viewModelScope

    val repository: Repository<CurrencyConversionRates> = RemoteRepositoryImpl()

    val currencyRatesLiveData: MutableLiveData<List<CurrencyConverter>> = MutableLiveData()

    init {
        startUpdatingCurrencyRates("GBP", 1.0)
    }

    fun startUpdatingCurrencyRates(baseCurrency: String, baseCurrencyAmount: Double): Job = currentScope.launch(Dispatchers.IO) {
//        stopUpdatingCurrencyRates()

        while (isActive) {
            updateCurrencyRatesEverySecond(baseCurrency, baseCurrencyAmount)
            delay(1000)
        }
    }

    private fun stopUpdatingCurrencyRates() {
//        if(currentScope.coroutineContext[Job] == Dispatchers.IO && currentScope.coroutineContext[Job]?.isActive == true) {
            currentScope.coroutineContext.cancelChildren()
//        }
    }

    private suspend fun updateCurrencyRatesEverySecond(baseCurrency: String, baseCurrencyAmount: Double) {
        val currencyRates: CurrencyConversionRates = repository.invokeWebService(baseCurrency)
        Log.i("getCurrencyRates", "getCurrencyRates called")
        currencyRatesLiveData.postValue(
            convertResponseToCurrencyConverter(currencyRates, baseCurrencyAmount)
        )

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

