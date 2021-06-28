package com.avidprogrammers.currencynotifier.ui.forex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.data.forex.ForexApiService
import com.avidprogrammers.currencynotifier.data.network.ConnectivityInterceptorImpl
import com.avidprogrammers.currencynotifier.data.network.ForexNetworkDataSourceImpl
import kotlinx.android.synthetic.main.forex_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Forex : Fragment() {

    var currencyNames = arrayOf("Select", "USD", "GBP", "EURO")
    var flags = intArrayOf(
        R.drawable.ic_select,
        R.drawable.ic_usd,
        R.drawable.ic_gbp,
        R.drawable.ic_euro
    )

    companion object {
        fun newInstance() = Forex()
    }

    private lateinit var viewModel: ForexViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forex_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForexViewModel::class.java)

        val apiService = ForexApiService(ConnectivityInterceptorImpl(this.requireContext()))
        val forexNetworkDataSource = ForexNetworkDataSourceImpl(apiService)

        forexNetworkDataSource.downloadedForex.observe(viewLifecycleOwner, Observer {
            forexValue.text = it.currencyVal.toString()
            Log.d("value", "value-${it.currencyVal}")
        })

        GlobalScope.launch(Dispatchers.Main) {
            forexNetworkDataSource.fetchCurrentValue("USDINR")
        }

    }

}