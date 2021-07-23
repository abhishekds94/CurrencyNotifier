package com.avidprogrammers.currencynotifier.ui.forex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.avidprogrammers.currencynotifier.data.forex.repository.ForexRepository
import kotlinx.coroutines.launch

class ForexViewModel(
    private val forexRepository: ForexRepository
) : ViewModel() {
    val forex = forexRepository.getCurrentForexValue().asLiveData()

    //    val forex by lazyDeferred {
//        forexRepository.getCurrentValue()
//        Log.d("[Forex123]","[ViewModel123] - "+forexRepository.getCurrentValue())
//    }
    init {
        viewModelScope.launch {
            val value = null
            value?.let { forexRepository.getCurrentValue(it) }
        }
    }

    fun selectedCurrencyPair(value: String) {
        viewModelScope.launch {
            forexRepository.getCurrentValue(value)
        }
    }


}