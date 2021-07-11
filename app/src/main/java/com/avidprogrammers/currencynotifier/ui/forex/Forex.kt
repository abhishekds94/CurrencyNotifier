package com.avidprogrammers.currencynotifier.ui.forex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.forex_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class Forex : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: ForexViewModelFactory by instance()

    private lateinit var viewModel: ForexViewModel

    private var sourceSelected = ""
    private var targetSelected = ""
    private var comboSelected = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forex_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ForexViewModel::class.java)

        btn_checkValue.setOnClickListener {
            if (sourceSelected == "Select Source") {
                Toast.makeText(context, "Select the Source Currency", Toast.LENGTH_LONG).show()
            } else if (targetSelected == "Select Target") {
                Toast.makeText(context, "Select the Target Currency", Toast.LENGTH_LONG).show()
            } else {
                bindUI()
            }
        }

        setForexSourceAdapterSpinner()
        setForexTargetAdapterSpinner()

    }

    private fun bindUI() = launch {
        Log.d("value", "value456")
        val currentValue = viewModel.forex
        currentValue.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            forexValue.text = it.currencyVal
            Log.d("value", "value-${it}")
        })
        comboSelected = sourceSelected + targetSelected
        Toast.makeText(context, "Combo-$comboSelected", Toast.LENGTH_SHORT).show()
    }

    private fun setForexSourceAdapterSpinner() {

        val sourceCountries = arrayListOf<SpinnerCountryData>()

        sourceCountries.add(SpinnerCountryData("Select Source", R.drawable.ic_select))
        sourceCountries.add(SpinnerCountryData("USD", R.drawable.ic_usd))
        sourceCountries.add(SpinnerCountryData("GBP", R.drawable.ic_gbp))
        sourceCountries.add(SpinnerCountryData("EURO", R.drawable.ic_euro))

        val adapter = SourceForexSpinnerAdapter(
            requireContext(),
            sourceCountries
        )

        forexSourceSpinner.adapter = adapter

        forexSourceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
//                Toast.makeText(context, "" + (parent?.getItemAtPosition(pos) as SourceCountryData).countryName, Toast.LENGTH_SHORT).show()
                sourceSelected =
                    ((parent?.getItemAtPosition(pos) as SpinnerCountryData).countryName)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun setForexTargetAdapterSpinner() {

        val targetCountries = arrayListOf<SpinnerCountryData>()

        targetCountries.add(SpinnerCountryData("Select Target", R.drawable.ic_select))
        targetCountries.add(SpinnerCountryData("INR", R.drawable.ic_usd))
        targetCountries.add(SpinnerCountryData("INR1", R.drawable.ic_gbp))
        targetCountries.add(SpinnerCountryData("INR2", R.drawable.ic_euro))

        val adapter = SourceForexSpinnerAdapter(
            requireContext(),
            targetCountries
        )

        forexTargetSpinner.adapter = adapter

        forexTargetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
//                Toast.makeText(context, "" + (parent?.getItemAtPosition(pos) as SourceCountryData).countryName, Toast.LENGTH_SHORT).show()
                targetSelected =
                    ((parent?.getItemAtPosition(pos) as SpinnerCountryData).countryName)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        adapter.notifyDataSetChanged()
    }

}