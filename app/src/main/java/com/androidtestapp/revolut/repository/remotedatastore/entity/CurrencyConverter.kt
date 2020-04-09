package com.androidtestapp.revolut.repository.remotedatastore.entity


import com.fasterxml.jackson.annotation.JsonProperty

data class CurrencyConverter(
    val baseCurrency: String,
    val rates: Rates
)