package com.androidtestapp.revolut.repository.remotedatastore.entity

import com.google.gson.annotations.SerializedName

data class BaseCurrencyToOtherCurrencyRates(
    @SerializedName("baseCurrency") val baseCurrency : String,
    @SerializedName("rates") val currencyRates : CurrencyRates
)