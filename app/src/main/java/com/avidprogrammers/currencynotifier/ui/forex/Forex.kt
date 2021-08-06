package com.avidprogrammers.currencynotifier.ui.forex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.forex_bottom_sheet.*
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
            when {
                sourceSelected == targetSelected -> {
                    Toast.makeText(
                        context,
                        "Source and Target Currencies cannot be same",
                        Toast.LENGTH_LONG
                    ).show()
                }
                sourceSelected == "Select Source" -> {
                    Toast.makeText(context, "Select the Source Currency", Toast.LENGTH_LONG).show()
                }
                targetSelected == "Select Target" -> {
                    Toast.makeText(context, "Select the Target Currency", Toast.LENGTH_LONG).show()
                }
                else -> {
                    bindUI()
                }
            }
        }

        setForexSourceAdapterSpinner()
        setForexTargetAdapterSpinner()

        setNotificationButton.setOnClickListener {
            val modalBottomSheetFragment = MyBottomSheetDialogFragment()
            modalBottomSheetFragment.show(childFragmentManager, modalBottomSheetFragment.tag)
        }

    }

    private fun bindUI() = launch {
        forexValueContainer.visibility = View.VISIBLE
        val currentValue = viewModel.forex
        currentValue.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            if (comboSelected == "USDBTC" || comboSelected == "GBPBTC" || comboSelected == "EURBTC") {
                forexValue.text = it.currencyVal
            } else {
                val number: Float = it.currencyVal.toFloat()
                val number2digits: Float = String.format("%.2f", number).toFloat()
                forexValue.text = number2digits.toString()
            }
        })
        comboSelected = sourceSelected + targetSelected
        Toast.makeText(context, "Combo-$comboSelected", Toast.LENGTH_SHORT).show()
        viewModel.selectedCurrencyPair(comboSelected)

        forexValText.text = getString(R.string.forexValText, sourceSelected, targetSelected)

    }

    private fun setForexSourceAdapterSpinner() {

        val sourceCountries = arrayListOf<SpinnerCountryData>()

        sourceCountries.add(SpinnerCountryData("Select Source", R.drawable.ic_select))
        sourceCountries.add(SpinnerCountryData("USD", R.drawable.ic_usd))
        sourceCountries.add(SpinnerCountryData("GBP", R.drawable.ic_gbp))
        sourceCountries.add(SpinnerCountryData("EUR", R.drawable.ic_euro))

        val adapter = SourceForexSpinnerAdapter(
            requireContext(),
            sourceCountries
        )

        forexSourceSpinner.adapter = adapter

        forexSourceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
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
        targetCountries.add(SpinnerCountryData("AED", R.drawable.ic_aed))
        targetCountries.add(SpinnerCountryData("ANG", R.drawable.ic_ang))
        targetCountries.add(SpinnerCountryData("ARS", R.drawable.ic_ars))
        targetCountries.add(SpinnerCountryData("AUD", R.drawable.ic_aud))

        targetCountries.add(SpinnerCountryData("BDT", R.drawable.ic_bdt))
        targetCountries.add(SpinnerCountryData("BRL", R.drawable.ic_brl))
        targetCountries.add(SpinnerCountryData("BTC", R.drawable.ic_btc))
        targetCountries.add(SpinnerCountryData("CAD", R.drawable.ic_cad))
        targetCountries.add(SpinnerCountryData("CHF", R.drawable.ic_chf))

        targetCountries.add(SpinnerCountryData("CNY", R.drawable.ic_cny))
        targetCountries.add(SpinnerCountryData("ETB", R.drawable.ic_etb))
        targetCountries.add(SpinnerCountryData("EUR", R.drawable.ic_euro))
        targetCountries.add(SpinnerCountryData("GBP", R.drawable.ic_gbp))
        targetCountries.add(SpinnerCountryData("INR", R.drawable.ic_inr))

        targetCountries.add(SpinnerCountryData("IQD", R.drawable.ic_iqd))
        targetCountries.add(SpinnerCountryData("IRR", R.drawable.ic_irr))
        targetCountries.add(SpinnerCountryData("JPY", R.drawable.ic_jpy))
        targetCountries.add(SpinnerCountryData("LKR", R.drawable.ic_lkr))
        targetCountries.add(SpinnerCountryData("MXN", R.drawable.ic_mxn))

        targetCountries.add(SpinnerCountryData("MYR", R.drawable.ic_myr))
        targetCountries.add(SpinnerCountryData("NGN", R.drawable.ic_ngn))
        targetCountries.add(SpinnerCountryData("RUB", R.drawable.ic_rub))
        targetCountries.add(SpinnerCountryData("USD", R.drawable.ic_usd))
        targetCountries.add(SpinnerCountryData("ZAR", R.drawable.ic_zar))

        val adapter = SourceForexSpinnerAdapter(
            requireContext(),
            targetCountries
        )

        forexTargetSpinner.adapter = adapter

        forexTargetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                targetSelected =
                    ((parent?.getItemAtPosition(pos) as SpinnerCountryData).countryName)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        adapter.notifyDataSetChanged()
    }
}