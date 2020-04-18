package com.androidtestapp.revolut.activity

import android.os.Build
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.androidtestapp.revolut.AppConstants
import com.androidtestapp.revolut.R
import com.androidtestapp.revolut.di.DependencyInjectionModule
import com.androidtestapp.revolut.repository.remote_repository.dto.CurrencyConverter
import com.androidtestapp.revolut.ui.activity.CurrencyConverterActivity
import com.androidtestapp.revolut.ui.adapter.CurrencyConverterAdapter
import com.androidtestapp.revolut.ui.viewmodel.CurrencyRateViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_currency_converter_list_item.*
import kotlinx.android.synthetic.main.adapter_currency_converter_list_item.view.*
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class CurrencyConverterActivityTest : KoinTest {
    companion object {
        private const val IS_NOT_VISIBLE = "is not visible"
        private const val IS_VISIBLE = "is visible"
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var viewModel: CurrencyRateViewModel

    private lateinit var activityScenario: ActivityScenario<CurrencyConverterActivity>

    @Mock
    private lateinit var liveData: LiveData<List<CurrencyConverter>>

    @Captor
    private lateinit var observerCaptor: ArgumentCaptor<Observer<List<CurrencyConverter>>>

    @Before
    fun setUp() {
        koinApplication {
            modules(
                listOf(
                    DependencyInjectionModule.viewModelModule,
                    DependencyInjectionModule.repositoryModule
                )
            )
        }

        viewModel = declareMock {
            BDDMockito.given(this.getCurrencyRates()).willReturn(liveData)

        }

        activityScenario = launchActivity()
        activityScenario.moveToState(Lifecycle.State.CREATED)

        Mockito.verify(liveData, Mockito.times(1))
            .observe(
                ArgumentMatchers.any(LifecycleOwner::class.java),
                observerCaptor.capture()
            )
    }

    @Test
    fun `has visible content loading progress bar view on create`() {

        activityScenario.onActivity { activity ->
            // Assert Content Loading Progress Bar is visible
            Assert.assertEquals(
                "contentLoadingProgressBar $IS_VISIBLE",
                View.VISIBLE,
                activity.contentLoadingProgressBar.visibility
            )
        }
    }

    @Test
    fun `has hidden other currency recycler view and base currency view on create`() {
        activityScenario.onActivity { activity ->
            // Assert Recycler View is not visible
            Assert.assertEquals(
                "currencyConverterRecyclerView $IS_NOT_VISIBLE",
                View.GONE,
                activity.currencyConverterRecyclerView.visibility
            )
            // Assert Base Currency View is not visible
            Assert.assertEquals(
                "includeLayout $IS_NOT_VISIBLE",
                View.GONE,
                activity.includeLayout.visibility
            )
        }
    }

    @Test
    fun `assert action bar title text`() {
        activityScenario.onActivity { activity ->
            val actionBarTitle = activity.getString(R.string.app_name)

            // Assert Action Bar Title text
            Assert.assertEquals(
                "ActionBar title is $actionBarTitle",
                actionBarTitle,
                activity.supportActionBar?.title
            )
        }
    }

    @Test
    fun `displays base currency view and other currency list when available`() {

        // Get Test Response
        val currencyConverterList = getTestResponse()

        // Update View Model observer with test response live data
        observerCaptor.value.onChanged(currencyConverterList)

        activityScenario.onActivity { activity ->
            // Assert Content Loading Progress Bar is not visible
            Assert.assertEquals(
                "contentLoadingProgressBar $IS_NOT_VISIBLE",
                View.GONE,
                activity.contentLoadingProgressBar.visibility
            )
            // Assert Recycler View is visible
            Assert.assertEquals(
                "currencyConverterRecyclerView $IS_VISIBLE",
                View.VISIBLE,
                activity.currencyConverterRecyclerView.visibility
            )
            // Assert Base Currency View is visible
            Assert.assertEquals(
                "includeLayout $IS_VISIBLE",
                View.VISIBLE,
                activity.includeLayout.visibility
            )
        }

    }

    @Test
    fun `validate Base Currency View Values`() {
        val currencyConverterList = getTestResponse()

        // Update View Model observer with test response live data
        observerCaptor.value.onChanged(currencyConverterList)

        activityScenario.onActivity { activity ->
            val currency = currencyConverterList[0]
            Assert.assertEquals(
                "Base currencyCodeTextView value is equal to first item's currencyCode in currencyConverterList",
                currency.currencyCode,
                activity.currencyCodeTextView.text
            )
            Assert.assertEquals(
                "Base currencyNameTextView value is equal to first item's currencyName in currencyConverterList",
                currency.currencyName,
                activity.currencyNameTextView.text
            )
            Assert.assertEquals(
                "Base currencyAmountEditText value is equal to first item's convertedAmount in currencyConverterList",
                currency.convertedAmount,
                activity.currencyAmountEditText.text.toString().toDouble(),
                AppConstants.CURRENCY_AMOUNT_ZERO
            )
        }
    }

    @Test
    fun `test recycler view adapter and its data`() {
        // Get Test Response
        val currencyConverterList = getTestResponse()

        // Update View Model observer with test response live data
        observerCaptor.value.onChanged(currencyConverterList)

        activityScenario.onActivity { activity ->

            // Assert other currency list in recycler view
            val mutableCurrencyConverterList = currencyConverterList.let {
                it.toMutableList().drop(1)
            }
            val adapterCurrencyConverterList =
                (activity.currencyConverterRecyclerView.adapter as? CurrencyConverterAdapter)?.currencyConverterList
            Assert.assertEquals(
                "CurrencyConverterRecyclerList in Adapter is as expected",
                mutableCurrencyConverterList,
                adapterCurrencyConverterList
            )

            val viewHolder =
                activity.currencyConverterRecyclerView.findViewHolderForAdapterPosition(0)
            val itemView = viewHolder?.itemView
            val currency = adapterCurrencyConverterList?.get(0)
            Assert.assertEquals(
                "ViewHolder currencyCodeTextView value is equal to first item's currencyCode in adapterCurrencyConverterList",
                currency?.currencyCode,
                itemView?.currencyCodeTextView?.text
            )
            Assert.assertEquals(
                "ViewHolder currencyNameTextView value is equal to first item's currencyName in adapterCurrencyConverterList",
                currency?.currencyName,
                itemView?.currencyNameTextView?.text
            )
            Assert.assertEquals(
                "ViewHolder currencyAmountEditText value is equal to first item's convertedAmount in adapterCurrencyConverterList",
                currency!!.convertedAmount,
                itemView?.currencyAmountEditText?.text.toString().toDouble(),
                AppConstants.CURRENCY_AMOUNT_ZERO
            )
        }
    }

    @After
    fun tearDown() {
        activityScenario.close()
        stopKoin()
    }

    private fun getTestResponse(): List<CurrencyConverter> {

        return listOf(
            CurrencyConverter(
                "https://www.countryflags.io/au/flat/64.png",
                "AUD",
                "Australian Dollar",
                1.0
            ),
            CurrencyConverter(
                "https://www.countryflags.io/bg/flat/64.png",
                "BGN",
                "Bulgarian Lev",
                5.0
            ),
            CurrencyConverter(
                "https://www.countryflags.io/br/flat/64.png",
                "BRL",
                "Brazilian Real",
                2.0
            )
        )
    }
}