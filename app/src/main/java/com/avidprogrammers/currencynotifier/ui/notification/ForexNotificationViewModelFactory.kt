package com.avidprogrammers.currencynotifier.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")

class ForexNotificationViewModelFactory(
    private val forexRepository: ForexNotificationRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForexNotificationViewModel(forexRepository) as T
    }
}