package com.androidtestapp.revolut.repository.remote_repository

import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remote_repository.webservice.WebService
import com.androidtestapp.revolut.repository.remote_repository.webservice.entity.CurrencyConversionRates

class RemoteRepositoryImpl(private val currencyRatesWebserviceImpl: WebService<CurrencyConversionRates>): Repository<CurrencyConversionRates> {

    override suspend fun invokeWebService(baseCurrency: String): CurrencyConversionRates?{
        return currencyRatesWebserviceImpl.executeWebService(baseCurrency)
    }
}