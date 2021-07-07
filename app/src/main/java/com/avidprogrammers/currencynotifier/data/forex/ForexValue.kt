package com.avidprogrammers.currencynotifier.data.forex

import android.app.Application
import com.avidprogrammers.currencynotifier.data.forex.repository.ForexRepository
import com.avidprogrammers.currencynotifier.data.forex.repository.ForexRepositoryImpl
import com.avidprogrammers.currencynotifier.data.network.ConnectivityInterceptor
import com.avidprogrammers.currencynotifier.data.network.ConnectivityInterceptorImpl
import com.avidprogrammers.currencynotifier.data.network.ForexNetworkDataSource
import com.avidprogrammers.currencynotifier.data.network.ForexNetworkDataSourceImpl
import com.avidprogrammers.currencynotifier.db.ForexDatabase
import com.avidprogrammers.currencynotifier.ui.forex.ForexViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForexValue : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForexValue))

        bind() from singleton { ForexDatabase(instance()) }
        bind() from singleton { instance<ForexDatabase>().ForexValueDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ForexApiService(instance()) }
        bind<ForexNetworkDataSource>() with singleton { ForexNetworkDataSourceImpl(instance()) }
        bind<ForexRepository>() with singleton { ForexRepositoryImpl(instance(), instance()) }
        bind() from provider { ForexViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}