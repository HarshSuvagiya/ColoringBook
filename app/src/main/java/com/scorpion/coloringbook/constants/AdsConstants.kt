package com.scorpion.coloringbook.constants

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.constants.interfaces.AdInterface
import com.scorpion.coloringbook.databinding.ActivityExitBinding

class AdsConstants {

    companion object {
        var INTER_ID = "ca-app-pub-3940256099942544/1033173712"
        var BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
        var APP_OPEN_ID = "ca-app-pub-3940256099942544/3419835294"
        var NATIVE_ID = "ca-app-pub-3940256099942544/2247696110"
        var REWARD_ID = "ca-app-pub-3940256099942544/5224354917"
        var TIMES_INTER_AD = 3L
        var COUNTER_INTER_AD = 3L
        var LOAD_AD = true
        var USE_TEST_AD_ID = false

        private var mInterstitialAd: InterstitialAd? = null
        var nativeAdmob: UnifiedNativeAd? = null

        private fun bannerAdSize(display: Display, ad_view_container: View, context: Context): AdSize {
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = ad_view_container.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
        }

        fun loadBanner(display: Display, ad_view_container: FrameLayout, context: Context) {
            if (LOAD_AD) {
                val adView = AdView(context)
                adView.adUnitId = BANNER_ID
                adView.adSize = bannerAdSize(display, ad_view_container, context)
                ad_view_container.addView(adView)
                val adRequest = AdRequest.Builder().build()
                adView.loadAd(adRequest)
            }
        }

        fun loadInter(context: Context) {
            var adRequest = AdRequest.Builder().build()
            InterstitialAd.load(context, INTER_ID, adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })
        }

        fun showInter(adInterface: AdInterface) {
            if (LOAD_AD) {
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(adInterface as Activity)
                } else {
                    adInterface.error()
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }

                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        adInterface.close()
                        loadInter(adInterface as Context)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        adInterface.error()
                    }

                    override fun onAdShowedFullScreenContent() {
                        adInterface.show()
                        mInterstitialAd = null
                    }
                }
            }
        }

        var adLoader: AdLoader? = null

        var nativeAdView: UnifiedNativeAdView? = null

        fun initNativeAdvanceAds(context: Context, binding: ActivityExitBinding) {
            flNativeAds = binding.nativeAd.flNativeAds
            flNativeAds!!.visibility = View.GONE
            nativeAdView = binding.nativeAd.adView
            nativeAdView!!.mediaView = nativeAdView!!.findViewById(R.id.ad_media)
            nativeAdView!!.headlineView = nativeAdView!!.findViewById(R.id.ad_headline)
            nativeAdView!!.bodyView = nativeAdView!!.findViewById(R.id.ad_body)
            nativeAdView!!.callToActionView = nativeAdView!!.findViewById(R.id.ad_call_to_action)
            nativeAdView!!.iconView = nativeAdView!!.findViewById(R.id.ad_icon)
            nativeAdView!!.starRatingView = nativeAdView!!.findViewById(R.id.ad_stars)
            nativeAdView!!.advertiserView = nativeAdView!!.findViewById(R.id.ad_advertiser)
            if (nativeAdmob == null)
                loadNativeAds(context)
            else {
                flNativeAds!!.visibility = View.VISIBLE
                populateNativeAdView(nativeAdmob!!, nativeAdView)
            }
        }

        fun populateNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView?) {
            val vc = nativeAd.videoController
            vc.videoLifecycleCallbacks = object : VideoLifecycleCallbacks() {
                override fun onVideoEnd() {
                    super.onVideoEnd()
                }
            }
            (adView!!.headlineView as TextView).text = nativeAd.headline
            (adView.bodyView as TextView).text = nativeAd.body
            (adView.callToActionView as Button).text = nativeAd.callToAction
            val icon = nativeAd.icon
            if (icon == null) {
                adView.iconView.visibility = View.INVISIBLE
            } else {
                (adView.iconView as ImageView).setImageDrawable(icon.drawable)
                adView.iconView.visibility = View.VISIBLE
            }
            if (nativeAd.starRating == null) {
                adView.starRatingView.visibility = View.INVISIBLE
            } else {
                (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
                adView.starRatingView.visibility = View.VISIBLE
            }
            if (nativeAd.advertiser == null) {
                adView.advertiserView.visibility = View.INVISIBLE
            } else {
                (adView.advertiserView as TextView).text = nativeAd.advertiser
                adView.advertiserView.visibility = View.VISIBLE
            }
            adView.setNativeAd(nativeAd)
        }

        var flNativeAds: FrameLayout? = null

        fun loadNativeAds(context: Context) {
            val videoOptions = VideoOptions.Builder()
                .setStartMuted(false)
                .build()
            val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()
            val builder: AdLoader.Builder = AdLoader.Builder(context, NATIVE_ID)
            adLoader = builder.forUnifiedNativeAd { unifiedNativeAd ->
                if (!adLoader!!.isLoading) {
                    flNativeAds!!.visibility = View.VISIBLE
                    populateNativeAdView(unifiedNativeAd, nativeAdView)
                }
            }.withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        Log.e(
                            "Exit", "The previous native ad failed to load. Attempting to"
                                    + " load another."
                        )
                        if (!adLoader!!.isLoading) {
                        }
                    }
                }).withNativeAdOptions(adOptions).build()

            // Load the Native ads.
            adLoader!!.loadAd(AdRequest.Builder().build())
        }

    }

}