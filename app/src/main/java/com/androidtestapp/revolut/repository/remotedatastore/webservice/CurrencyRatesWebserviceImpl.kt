package com.androidtestapp.revolut.repository.remotedatastore.webservice

import com.androidtestapp.revolut.repository.remotedatastore.entity.CurrencyConversionRates
import com.androidtestapp.revolut.repository.remotedatastore.networking.NetworkCommunication
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.ResponseBody

class CurrencyRatesWebserviceImpl : WebService<CurrencyConversionRates> {

    companion object {
        private const val CURRENCY_RATES_URL =
            "https://hiring.revolut.codes/api/android/latest?base="
    }

    override suspend fun executeWebService(baseCurrency: String): CurrencyConversionRates {
        return NetworkCommunication
            .getNetworkCommServiceResponse(CURRENCY_RATES_URL + baseCurrency)
            .let { response ->
                when (val code = response.code()) {
                    in 200..299 -> parseResponse(response.body()!!)
                    else -> throw Exception("Unexpected Response Code: $code")
                }

            }
    }

    private fun parseResponse(responseBody: ResponseBody): CurrencyConversionRates{
        val mapper = jacksonObjectMapper()
        return mapper.readValue(responseBody.string())
    }

}