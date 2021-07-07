package com.avidprogrammers.currencynotifier.ui.forex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        bindUI()

        Log.d("value", "value123")
    }

    private fun bindUI() = launch {
        Log.d("value", "value456")
        val currentValue = viewModel.forex.await()
        currentValue.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            forexValue.text = it.toString()
            Log.d("value", "value-${it}")
        })
    }.cancel()

}