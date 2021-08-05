package com.avidprogrammers.currencynotifier.ui.crypto

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.avidprogrammers.currencynotifier.R
import kotlinx.android.synthetic.main.crypto_fragment.*

class Crypto : Fragment() {

    companion object {
        fun newInstance() = Crypto()
    }

    private lateinit var viewModel: CryptoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.crypto_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CryptoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}