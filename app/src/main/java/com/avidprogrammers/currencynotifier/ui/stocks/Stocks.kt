package com.avidprogrammers.currencynotifier.ui.stocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.avidprogrammers.currencynotifier.R

class Stocks : Fragment() {

    companion object {
        fun newInstance() = Stocks()
    }

    private lateinit var viewModel: StocksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stocks_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StocksViewModel::class.java)
        // TODO: Use the ViewModel
    }

}