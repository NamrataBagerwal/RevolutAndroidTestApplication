package com.androidtestapp.revolut.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remotedatastore.entity.BaseCurrencyToOtherCurrencyRates
import kotlinx.coroutines.launch

class CurrencyRateViewModel(application: Application, private val repository: Repository<BaseCurrencyToOtherCurrencyRates>): AndroidViewModel(application) {

    val currencyRatesLiveData: MutableLiveData<BaseCurrencyToOtherCurrencyRates> = MutableLiveData()

    fun getCurrencyRates() = viewModelScope.launch {
        val currencyRates = repository.invokeHeavyOperation()
        currencyRatesLiveData.postValue(currencyRates)


//        val map = ObjectMapper().readValue<MutableMap<Any, Any>>(jsonString)
    }
}