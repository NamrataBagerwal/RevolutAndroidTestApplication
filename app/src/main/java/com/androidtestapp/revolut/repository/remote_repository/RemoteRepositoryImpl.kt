package com.androidtestapp.revolut.repository.remote_repository

import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remote_repository.webservice.entity.CurrencyConversionRates
import com.androidtestapp.revolut.repository.remote_repository.webservice.CurrencyRatesWebserviceImpl

class RemoteRepositoryImpl: Repository<CurrencyConversionRates> {

    override suspend fun invokeWebService(baseCurrency: String): CurrencyConversionRates?{
        return CurrencyRatesWebserviceImpl().executeWebService(baseCurrency)
    }
}