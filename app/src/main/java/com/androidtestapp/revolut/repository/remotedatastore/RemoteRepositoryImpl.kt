package com.androidtestapp.revolut.repository.remotedatastore

import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remotedatastore.entity.BaseCurrencyToOtherCurrencyRates
import com.androidtestapp.revolut.repository.remotedatastore.entity.CurrencyConversionRates
import com.androidtestapp.revolut.repository.remotedatastore.webservice.CurrencyRatesWebserviceImpl

class RemoteRepositoryImpl: Repository<CurrencyConversionRates> {

    override suspend fun invokeWebService(baseCurrency: String): CurrencyConversionRates{
        return CurrencyRatesWebserviceImpl().executeWebService(baseCurrency)
    }
}