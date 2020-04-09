package com.androidtestapp.revolut.repository.remotedatastore.entity


import com.fasterxml.jackson.annotation.JsonProperty

data class Rates(
    @JsonProperty("AUD")
    val aUD: Double,
    @JsonProperty("BGN")
    val bGN: Double,
    @JsonProperty("BRL")
    val bRL: Double,
    @JsonProperty("CAD")
    val cAD: Double,
    @JsonProperty("CHF")
    val cHF: Double,
    @JsonProperty("CNY")
    val cNY: Double,
    @JsonProperty("CZK")
    val cZK: Double,
    @JsonProperty("DKK")
    val dKK: Double,
    @JsonProperty("GBP")
    val gBP: Double,
    @JsonProperty("HKD")
    val hKD: Double,
    @JsonProperty("HRK")
    val hRK: Double,
    @JsonProperty("HUF")
    val hUF: Double,
    @JsonProperty("IDR")
    val iDR: Double,
    @JsonProperty("ILS")
    val iLS: Double,
    @JsonProperty("INR")
    val iNR: Double,
    @JsonProperty("ISK")
    val iSK: Double,
    @JsonProperty("JPY")
    val jPY: Double,
    @JsonProperty("KRW")
    val kRW: Double,
    @JsonProperty("MXN")
    val mXN: Double,
    @JsonProperty("MYR")
    val mYR: Double,
    @JsonProperty("NOK")
    val nOK: Double,
    @JsonProperty("NZD")
    val nZD: Double,
    @JsonProperty("PHP")
    val pHP: Double,
    @JsonProperty("PLN")
    val pLN: Double,
    @JsonProperty("RON")
    val rON: Double,
    @JsonProperty("RUB")
    val rUB: Double,
    @JsonProperty("SEK")
    val sEK: Double,
    @JsonProperty("SGD")
    val sGD: Double,
    @JsonProperty("THB")
    val tHB: Double,
    @JsonProperty("USD")
    val uSD: Double,
    @JsonProperty("ZAR")
    val zAR: Double
)