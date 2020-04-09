package com.androidtestapp.revolut.repository.remotedatastore.webservice

interface WebService<T> {
    suspend fun executeWebService(): T
}