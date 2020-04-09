package com.androidtestapp.revolut.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remotedatastore.RemoteRepositoryImpl
import com.androidtestapp.revolut.repository.remotedatastore.dto.CurrencyConverter
import com.androidtestapp.revolut.repository.remotedatastore.dto.CurrencyEnum
import com.androidtestapp.revolut.repository.remotedatastore.entity.CurrencyConversionRates
import kotlinx.coroutines.launch

class CurrencyRateViewModel(): ViewModel() {

    val currencyRatesLiveData: MutableLiveData<List<CurrencyConverter>> = MutableLiveData()

    fun getCurrencyRates(baseCurrency: String, baseCurrencyAmount: Double) = viewModelScope.launch {

        currencyRatesLiveData.postValue(
            convertResponseToCurrencyConverter(baseCurrency, baseCurrencyAmount)
        )

    }

    private suspend fun convertResponseToCurrencyConverter(baseCurrency: String, baseCurrencyAmount: Double): List<CurrencyConverter>{
        val repository: Repository<CurrencyConversionRates> = RemoteRepositoryImpl()

        val currencyRates: CurrencyConversionRates = repository.invokeWebService(baseCurrency)

        val currencyConverterList: MutableList<CurrencyConverter> = mutableListOf()

        val baseCurrencyRate: String = currencyRates.baseCurrency
        var currency: CurrencyEnum = CurrencyEnum.getCurrencyByCode(baseCurrencyRate)
        var currencyConverter: CurrencyConverter = CurrencyConverter(
            currency.getCurrencyFlag(), currency.currencyName, currency.currencyCode, baseCurrencyAmount
        )
        currencyConverterList.add(currencyConverter)

        val rates: LinkedHashMap<String, Double> = currencyRates.rates
        for(key in rates.keys){
            currency = CurrencyEnum.getCurrencyByCode(key)
            currencyConverter = CurrencyConverter(
                currency.getCurrencyFlag(), currency.currencyName, currency.currencyCode, baseCurrencyAmount.times(rates[key])
            )
            currencyConverterList.add(currencyConverter)
        }
        return currencyConverterList

    }
}

private operator fun Double.times(d: Double?): Double {
    return this * d
}
