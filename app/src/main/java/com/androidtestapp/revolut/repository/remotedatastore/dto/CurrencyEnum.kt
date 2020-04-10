package com.androidtestapp.revolut.repository.remotedatastore.dto

enum class CurrencyEnum(
    val currencyCode: String,
    val currencyName: String
) {

    AUD("AUD","Australian Dollar") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    BGN("BGN","Bulgarian Lev") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    BRL("BRL","Brazilian Real") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    CAD("CAD","Canadian Dollar") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    CHF("CHF","Swiss Franc"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    CNY("CNY","Chinese Yuan"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    CZK("CZK","Czech Koruna"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    DKK("DKK","Danish Krone"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    EUR("EUR","Euro"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    GBP("GBP", "British Pound Sterling"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    HKD("HKD","Hong Kong Dollar"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    HRK("HRK","Croatian Kuna"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    HUF("HUF", "Hungarian Forint"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    IDR("IDR", "Indonesian Rupiah"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    ILS("ILS", "Israeli New Shekel"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    INR("INR", "Indian Rupee"){
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)
        }
    },
    ISK("ISK","Icelandic Króna") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    JPY("JPY","Japanese Yen") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    KRW("KRW","South Korean won") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    MXN("MXN","Mexican Peso") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    MYR("MYR","Malaysian Ringgit") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    NOK("NOK","Norwegian Krone") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    NZD("NZD","New Zealand Dollar") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    PHP("PHP","Philippine peso") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    PLN("PLN","Poland złoty") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    RON("RON","Romanian Leu") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    RUB("RUB","Russian Ruble") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    SEK("SEK","Swedish Krona") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    SGD("SGD","Singapore Dollar") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    THB("THB","Thai Baht") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    USD("USD","United States Dollar") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    },
    ZAR("ZAR","South African Rand") {
        override fun getCurrencyFlag(): String {
            return prepareCurrencyFlagUrl(currencyCode)

        }
    };



    companion object {
        fun getCurrencyByCode(name: String) = valueOf(name.toUpperCase())
    }

    internal fun prepareCurrencyFlagUrl(currencyCode: String): String{
        val currencyFlag = currencyCode.subSequence(0..1).toString().toLowerCase()
        return "https://www.countryflags.io/$currencyFlag/flat/64.png"
    }

    abstract fun getCurrencyFlag(): String
}