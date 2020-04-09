package com.androidtestapp.revolut.repository.remotedatastore.entity

data class CurrencyConversionRates(
    val baseCurrency: String,
    val rates: LinkedHashMap<String, Double>
)