package com.androidtestapp.revolut.repository.remote_repository.dto

data class CurrencyConverter(
 var currencyFlag: String,
 var currencyCode: String,
 var currencyName: String,
 var convertedAmount: Double
)