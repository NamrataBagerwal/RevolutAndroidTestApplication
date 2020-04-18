package com.androidtestapp.revolut

import android.app.Application
import com.androidtestapp.revolut.di.DependencyInjectionModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CurrencyRatesApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // get list of all modules
        val diModuleList = listOf(
            DependencyInjectionModule.repositoryModule,
            DependencyInjectionModule.viewModelModule
        )
        // start koin with the module list
        startKoin {
            // Android context
            androidContext(this@CurrencyRatesApplication)
            // modules
            modules(diModuleList)
        }
    }
}