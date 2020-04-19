package com.androidtestapp.revolut.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.androidtestapp.revolut.AppConstants
import com.androidtestapp.revolut.di.DependencyInjectionModule
import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remote_repository.dto.CurrencyConverter
import com.androidtestapp.revolut.repository.remote_repository.webservice.entity.CurrencyConversionRates
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class CurrencyRateViewModelTest : KoinTest {

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val viewModel: CurrencyRateViewModel by inject()
    private lateinit var repository: Repository<CurrencyConversionRates>
    @Mock
    private lateinit var observer: Observer<List<CurrencyConverter>>

    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    DependencyInjectionModule.viewModelModule,
                    DependencyInjectionModule.repositoryModule
                )
            )
        }

        repository = declareMock()

        runBlocking {
            `when`(repository.invokeWebService(ArgumentMatchers.anyString())).thenReturn(
                getTestResponse()
            )
        }

    }

    @Test
    fun `verify and assert argument of repository invoke webservice call`() {

        val argCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)

        runBlockingTest {
            pauseDispatcher()

            viewModel.updateCurrencyRates(
                TEST_DEFAULT_BASE_CURRENCY,
                TEST_DEFAULT_BASE_CURRENCY_AMOUNT
            )

            runCurrent()

            advanceTimeBy(DEFAULT_ADVANCE_DELAY_TIME)

            resumeDispatcher()

            pauseDispatcher()

            verify(repository).invokeWebService(argCaptor.capture())

            runCurrent()

            advanceTimeBy(DEFAULT_ADVANCE_DELAY_TIME)

            resumeDispatcher()

            val capturedValue = argCaptor.value
            assertEquals(
                "Default Base Currency is as expected",
                TEST_DEFAULT_BASE_CURRENCY,
                capturedValue
            )
        }
    }

    @Test
    fun `assert observer onchanged is called`() {

        viewModel.getCurrencyRates().observeForever(observer)

        runBlockingTest {
            pauseDispatcher()

            viewModel.updateCurrencyRates(
                TEST_DEFAULT_BASE_CURRENCY,
                TEST_DEFAULT_BASE_CURRENCY_AMOUNT
            )

            runCurrent()

            advanceTimeBy(DEFAULT_ADVANCE_DELAY_TIME)

            resumeDispatcher()

            val currencyConversionList: List<CurrencyConverter>? = viewModel.getCurrencyRates().value

            // Update View Model observer with test response live data
//            observerCaptor.value.onChanged(currencyConversionList)

            @Captor
            val observerCaptor: ArgumentCaptor<<List<CurrencyConverter>>

            verify(observer).onChanged(observerCaptor.capture())

        }
    }

    @Test
    fun `assert live data value returned by observer`() {

        val testResponse = getTestResponse()

        viewModel.getCurrencyRates().observeForever(observer)

        runBlockingTest {

            pauseDispatcher()

            viewModel.updateCurrencyRates(
                TEST_DEFAULT_BASE_CURRENCY,
                TEST_DEFAULT_BASE_CURRENCY_AMOUNT
            )

            runCurrent()

            advanceTimeBy(DEFAULT_ADVANCE_DELAY_TIME)

            resumeDispatcher()

            val currencyConversionList: List<CurrencyConverter>? = viewModel.getCurrencyRates().value

            // assert the values
            val baseCurrency = currencyConversionList?.get(0)

            assertEquals(
                "Result Base Currency Code is equal to test response Base Currency Code",
                testResponse.baseCurrency,
                baseCurrency?.currencyCode
            )
            assertEquals(
                "Result Base Currency Rate is equal to test response Base Currency Value",
                TEST_DEFAULT_BASE_CURRENCY_AMOUNT,
                baseCurrency?.convertedAmount
            )

            val currency1 = currencyConversionList?.get(1)
            val rates = testResponse.rates
            assertTrue(
                "Result Currency1 Code is key present in test response",
                rates.containsKey(currency1?.currencyCode)

            )
            assertEquals(
                "Result Currency1 Rate is equal to test response Currency1 Rate",
                rates[currency1?.currencyCode],
                currency1?.convertedAmount
            )

            val currency2 = currencyConversionList?.get(2)
            assertTrue(
                "Result Currency2 Code is key present in test response",
                rates.containsKey(currency2?.currencyCode)

            )
            assertEquals(
                "Result Currency2 Rate is equal to test response Currency2 Rate",
                rates[currency2?.currencyCode],
                currency2?.convertedAmount
            )

            val currency3 = currencyConversionList?.get(3)
            assertTrue(
                "Result Currency3 Code is key present in test response",
                rates.containsKey(currency3?.currencyCode)

            )
            assertEquals(
                "Result Currency3 Rate is equal to test response Currency3 Rate",
                rates[currency3?.currencyCode],
                currency3?.convertedAmount
            )

        }

    }

    @After
    fun tearDown() {
        stopKoin()
    }

    private fun getTestResponse(): CurrencyConversionRates {
        val baseCurrency = TEST_DEFAULT_BASE_CURRENCY

        val hashMap = LinkedHashMap<String, Double>()
        hashMap[TEST_CURRENCY_1] = TEST_CURRENCY_1_RATE
        hashMap[TEST_CURRENCY_2] = TEST_CURRENCY_2_RATE
        hashMap[TEST_CURRENCY_3] = TEST_CURRENCY_3_RATE

        return CurrencyConversionRates(baseCurrency, hashMap)
    }

    companion object {
        const val TEST_DEFAULT_BASE_CURRENCY = AppConstants.DEFAULT_BASE_CURRENCY
        const val TEST_DEFAULT_BASE_CURRENCY_AMOUNT = AppConstants.DEFAULT_BASE_CURRENCY_AMOUNT
        const val TEST_CURRENCY_1 = "AUD"
        const val TEST_CURRENCY_1_RATE = 7.0
        const val TEST_CURRENCY_2 = "BGN"
        const val TEST_CURRENCY_2_RATE = 8.0
        const val TEST_CURRENCY_3 = "BRL"
        const val TEST_CURRENCY_3_RATE = 5.0
        const val DEFAULT_ADVANCE_DELAY_TIME: Long = 6000
        const val LIST_FIRST_INDEX = 0
        const val LIST_SECOND_INDEX = 1
        const val LIST_THIRD_INDEX = 2
        const val LIST_FOURTH_INDEX = 3
    }
}