package com.androidtestapp.revolut.repository.remote_repository.webservice

import android.util.Log
import com.androidtestapp.revolut.repository.remote_repository.networking.NetworkCommunication
import com.androidtestapp.revolut.repository.remote_repository.webservice.entity.CurrencyConversionRates
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.ResponseBody

class CurrencyRatesWebserviceImpl() : WebService<CurrencyConversionRates> {

    companion object {
        private const val CURRENCY_RATES_URL =
            "https://hiring.revolut.codes/api/android/latest?base="
        private val TAG = CurrencyRatesWebserviceImpl::class.simpleName
    }

    override suspend fun executeWebService(baseCurrency: String): CurrencyConversionRates? {
        return NetworkCommunication
            .getNetworkCommServiceResponse(CURRENCY_RATES_URL + baseCurrency)
            .let { response ->
                when (val code = response.code) {
                    in 200..299 -> parseResponse(response.body!!)
                    else -> throw Exception("Unexpected Response Code: $code")
                }

            }
    }

    private fun parseResponse(responseBody: ResponseBody): CurrencyConversionRates?{
        var currencyConversionRates: CurrencyConversionRates? = null
        try{
            val mapper = jacksonObjectMapper()
            currencyConversionRates =  mapper.readValue(responseBody.string())
        }catch (exception: Exception){
            Log.e(TAG, exception.message)
        }
        return currencyConversionRates
    }

}