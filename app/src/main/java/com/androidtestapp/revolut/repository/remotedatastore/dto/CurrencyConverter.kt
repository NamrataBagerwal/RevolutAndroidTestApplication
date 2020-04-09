package com.androidtestapp.revolut.repository.remotedatastore.dto

data class CurrencyConverter(
 var currencyFlag: String,
 var currencyCode: String,
 var currencyName: String,
 var currencyRate: Double
)