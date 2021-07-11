package com.avidprogrammers.currencynotifier.ui.forex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.avidprogrammers.currencynotifier.R
import kotlinx.android.synthetic.main.spinner_items.view.*
import java.util.*

class SourceForexSpinnerAdapter(ctx: Context, countries: ArrayList<SpinnerCountryData>) :
    ArrayAdapter<SpinnerCountryData>
        (ctx, 0, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    fun createItemView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val country = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_items,
            parent,
            false
        )

        country?.let {
            view.sourceForexImage.setImageResource(country.flag)
            view.sourceForexName.text = country.countryName
        }
        return view
    }
}