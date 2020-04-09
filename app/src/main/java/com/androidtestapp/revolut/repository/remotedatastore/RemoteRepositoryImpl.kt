package com.androidtestapp.revolut.repository.remotedatastore

import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remotedatastore.entity.BaseCurrencyToOtherCurrencyRates
import com.androidtestapp.revolut.repository.remotedatastore.webservice.CurrencyRatesWebserviceImpl

class RemoteRepositoryImpl: Repository<BaseCurrencyToOtherCurrencyRates> {

    override suspend fun invokeHeavyOperation(): BaseCurrencyToOtherCurrencyRates{
        return CurrencyRatesWebserviceImpl().executeWebService()
    }
}