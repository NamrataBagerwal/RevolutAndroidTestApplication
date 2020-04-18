package com.androidtestapp.revolut.repository.remote_repository.webservice.entity

data class CurrencyConversionRates(
    val baseCurrency: String,
    val rates: LinkedHashMap<String, Double>
)