package com.avidprogrammers.currencynotifier.ui.crypto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.ui.forex.firebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

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

        firebaseAnalytics = Firebase.analytics

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "crypto")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

    }

}