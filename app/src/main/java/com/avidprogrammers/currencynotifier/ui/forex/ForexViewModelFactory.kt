package com.avidprogrammers.currencynotifier.ui.forex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avidprogrammers.currencynotifier.data.forex.repository.ForexRepository

@Suppress("UNCHECKED_CAST")

class ForexViewModelFactory(
    private val forexRepository: ForexRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForexViewModel(forexRepository) as T
    }
}