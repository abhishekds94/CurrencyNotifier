package com.avidprogrammers.currencynotifier.ui.forex

import com.avidprogrammers.currencynotifier.R

enum class SourceCountry(val countryCode: String, val icon: Int) {
    SELECT_SOURCE("Select Source", R.drawable.ic_select),
    USD("USD", R.drawable.ic_usd),
    GBP("GBP", R.drawable.ic_gbp),
    EURO("EURO", R.drawable.ic_euro),
}
