package com.androidtestapp.revolut.repository

interface Repository<T> {
    suspend fun invokeWebService(baseCurrency: String): T?
}