package com.androidtestapp.revolut.repository.remote_repository.webservice

interface WebService<T> {
    suspend fun executeWebService(baseCurrency: String): T?
}