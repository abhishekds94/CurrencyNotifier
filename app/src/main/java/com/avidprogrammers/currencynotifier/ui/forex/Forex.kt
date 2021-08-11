package com.avidprogrammers.currencynotifier.ui.forex

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avidprogrammers.currencynotifier.BuildConfig
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.ui.SnackbarUtil
import com.avidprogrammers.currencynotifier.ui.base.ScopedFragment
import com.avidprogrammers.currencynotifier.ui.notification.NotificationActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.android.synthetic.main.forex_bottom_sheet.*
import kotlinx.android.synthetic.main.forex_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

const val AD_UNIT_ID = BuildConfig.ADMOB_INTERSTITIAL
const val ADMOB_AD_UNIT_ID = BuildConfig.ADMOB_NATIVE
var currentNativeAd: NativeAd? = null

class Forex : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: ForexViewModelFactory by instance()

    private lateinit var viewModel: ForexViewModel

    private var sourceSelected = ""
    private var targetSelected = ""
    private var comboSelected = ""

    private var mInterstitialAd: InterstitialAd? = null
    private var mAdIsLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        refreshAd()

        return inflater.inflate(R.layout.forex_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ForexViewModel::class.java)

        MobileAds.initialize(requireContext()) {}

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("ABCDEF012345"))
                .build()
        )

        if (!mAdIsLoading && mInterstitialAd == null) {
            mAdIsLoading = true
            loadAd()
        }

        iv_notificationStatus.setOnClickListener{
            val intent= Intent(requireContext(),NotificationActivity::class.java)
            requireContext().startActivity(intent)
        }

        btn_checkValue.setOnClickListener {
            when {
                sourceSelected == targetSelected -> {
                    SnackbarUtil.showErrorSnack(requireActivity(), "Source and Target Currencies cannot be same!")
                }
                sourceSelected == "Select Source" -> {
                    SnackbarUtil.showErrorSnack(requireActivity(), "Select the Source Currency!")
                }
                targetSelected == "Select Target" -> {
                    SnackbarUtil.showErrorSnack(requireActivity(), "Select the Target Currency!")
                }
                else -> {
                    refreshAd()
                    showInterstitial()
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

    private fun loadAd() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context, AD_UNIT_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("TAG", adError?.message)
                    mInterstitialAd = null
                    mAdIsLoading = false
                    val error = "domain: ${adError.domain}, code: ${adError.code}, " +
                            "message: ${adError.message}"
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("TAG", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    mAdIsLoading = false
                }
            }
        )
    }

    private fun showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("TAG", "Ad was dismissed.")
                    mInterstitialAd = null
                    loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Log.d("TAG", "Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("TAG", "Ad showed fullscreen content.")
                    // Called when ad is dismissed.
                }
            }
            mInterstitialAd?.show(requireActivity())
        } else {
        }
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set the media view.
        adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
//        adView.iconView = adView.findViewById(R.id.ad_app_icon)
//        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
//        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView.setMediaContent(nativeAd.mediaContent)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        adView.setNativeAd(nativeAd)

        val vc = nativeAd.mediaContent.videoController

        if (vc.hasVideoContent()) {
            vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                override fun onVideoEnd() {
                    super.onVideoEnd()
                }
            }
        } else {

        }
    }

    private fun refreshAd() {

        val builder = AdLoader.Builder(context, ADMOB_AD_UNIT_ID)

        builder.forNativeAd { nativeAd ->
            var activityDestroyed = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            }
            if (activityDestroyed ) {
                nativeAd.destroy()
                return@forNativeAd
            }
            currentNativeAd?.destroy()
            currentNativeAd = nativeAd
            val adView = layoutInflater
                .inflate(R.layout.native_ad, null) as NativeAdView
            populateNativeAdView(nativeAd, adView)
            ad_frame.removeAllViews()
            ad_frame.addView(adView)
        }

        val videoOptions = VideoOptions.Builder()
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()

        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                val error =
                    """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())

    }

    override fun onDestroy() {
        currentNativeAd?.destroy()
        super.onDestroy()
    }

}