package com.androidtestapp.revolut.repository.remotedatastore.webservice

import com.androidtestapp.revolut.repository.remotedatastore.entity.BaseCurrencyToOtherCurrencyRates
import com.androidtestapp.revolut.repository.remotedatastore.networking.NetworkCommunication
import com.google.gson.Gson
import okhttp3.ResponseBody

class CurrencyRatesWebserviceImpl : WebService<BaseCurrencyToOtherCurrencyRates> {

    companion object {
        private const val CURRENCY_RATES_URL =
            "https://hiring.revolut.codes/api/android/latest?base=EUR"
    }

    override suspend fun executeWebService(): BaseCurrencyToOtherCurrencyRates {
        return NetworkCommunication
            .getNetworkCommServiceResponse(CURRENCY_RATES_URL)
            .let { response ->
                when (val code = response.code()) {
                    in 200..299 -> parseResponse(response.body()!!)
                    else -> throw Exception("Unexpected Response Code: $code")
                }

            }
    }

    private fun parseResponse(responseBody: ResponseBody): BaseCurrencyToOtherCurrencyRates{
        return Gson().fromJson(responseBody.string(), BaseCurrencyToOtherCurrencyRates::class.java)
    }

}