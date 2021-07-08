package com.avidprogrammers.currencynotifier.data.network

import androidx.lifecycle.LiveData
import com.avidprogrammers.currencynotifier.data.network.response.ForexResponse

interface ForexNetworkDataSource {
    val downloadedForex: LiveData<ForexResponse>

    suspend fun fetchCurrentValue(
        currencyCode: String
    )
}