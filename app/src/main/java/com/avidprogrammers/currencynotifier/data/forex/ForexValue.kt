package com.avidprogrammers.currencynotifier.data.forex

import android.app.Application
import com.avidprogrammers.currencynotifier.data.forex.repository.ForexRepository
import com.avidprogrammers.currencynotifier.data.forex.repository.ForexRepositoryImpl
import com.avidprogrammers.currencynotifier.data.network.ConnectivityInterceptor
import com.avidprogrammers.currencynotifier.data.network.ConnectivityInterceptorImpl
import com.avidprogrammers.currencynotifier.data.network.ForexNetworkDataSource
import com.avidprogrammers.currencynotifier.data.network.ForexNetworkDataSourceImpl
import com.avidprogrammers.currencynotifier.db.ForexDatabase
import com.avidprogrammers.currencynotifier.ui.ads.AppOpenManager
import com.avidprogrammers.currencynotifier.ui.forex.ForexViewModelFactory
import com.avidprogrammers.currencynotifier.ui.notification.ForexNotificationRepository
import com.avidprogrammers.currencynotifier.ui.notification.ForexNotificationViewModelFactory
import com.google.android.gms.ads.MobileAds
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

        bind() from this.singleton { ForexDatabase(this.instance()) }
        bind() from this.singleton { this.instance<ForexDatabase>().ForexValueDao() }
        this.bind<ConnectivityInterceptor>() with this.singleton { ConnectivityInterceptorImpl(this.instance()) }
        bind() from this.singleton { ForexApiService(this.instance()) }
        this.bind<ForexNetworkDataSource>() with this.singleton { ForexNetworkDataSourceImpl(this.instance()) }
        this.bind<ForexRepository>() with this.singleton {
            ForexRepositoryImpl(
                this.instance(),
                this.instance()
            )
        }
        this.bind<ForexNotificationRepository>() with this.singleton {
            ForexNotificationRepository(this.instance())
        }
        bind() from this.provider { ForexViewModelFactory(this.instance()) }
        bind() from this.provider { ForexNotificationViewModelFactory(this.instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}