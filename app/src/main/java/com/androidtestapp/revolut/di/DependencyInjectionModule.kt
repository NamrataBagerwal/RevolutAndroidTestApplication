package com.androidtestapp.revolut.di

import com.androidtestapp.revolut.repository.Repository
import com.androidtestapp.revolut.repository.remote_repository.RemoteRepositoryImpl
import com.androidtestapp.revolut.repository.remote_repository.webservice.entity.CurrencyConversionRates
import com.androidtestapp.revolut.ui.viewmodel.CurrencyRateViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


object DependencyInjectionModule {

    val repositoryModule = module {
        single<Repository<CurrencyConversionRates>> {
            return@single RemoteRepositoryImpl()
        }
    }

    val viewModelModule = module {
        viewModel { CurrencyRateViewModel(get()) }
    }


}