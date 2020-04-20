package com.androidtestapp.revolut.di

import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remote_repository.RemoteRepositoryImpl
import com.androidtestapp.revolut.repository.remote_repository.webservice.CurrencyRatesWebserviceImpl
import com.androidtestapp.revolut.repository.remote_repository.webservice.WebService
import com.androidtestapp.revolut.repository.remote_repository.webservice.entity.CurrencyConversionRates
import com.androidtestapp.revolut.ui.viewmodel.CurrencyConversionViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


object DependencyInjectionModule {

    val repositoryModule = module {
        single<Repository<CurrencyConversionRates>> {
            return@single RemoteRepositoryImpl(get())
        }
    }

    val webserviceModule =  module{
        single<WebService<CurrencyConversionRates>> {
            return@single CurrencyRatesWebserviceImpl()
        }
    }

    val viewModelModule = module {
        viewModel { CurrencyConversionViewModel(get()) }
    }


}