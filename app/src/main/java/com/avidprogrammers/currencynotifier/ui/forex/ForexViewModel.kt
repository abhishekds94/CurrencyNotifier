package com.avidprogrammers.currencynotifier.ui.forex

import androidx.lifecycle.ViewModel
import com.avidprogrammers.currencynotifier.data.forex.repository.ForexRepository
import com.avidprogrammers.currencynotifier.internal.lazyDeferred

class ForexViewModel(
    private val forexRepository: ForexRepository
) : ViewModel() {
    val forex by lazyDeferred {
        forexRepository.getCurrentValue()
    }
}