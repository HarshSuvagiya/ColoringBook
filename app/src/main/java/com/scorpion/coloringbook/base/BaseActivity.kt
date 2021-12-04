package com.scorpion.coloringbook.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.MobileAds
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.constants.AdsConstants
import com.scorpion.coloringbook.constants.interfaces.AdInterface
import android.view.animation.AlphaAnimation
import com.scorpion.coloringbook.constants.AdsConstants.Companion.COUNTER_INTER_AD
import com.scorpion.coloringbook.constants.AdsConstants.Companion.TIMES_INTER_AD


open class BaseActivity : AppCompatActivity(), AdInterface {

    val buttonClick = AlphaAnimation(1f, 0.8f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        initBanner()
        AdsConstants.loadInter(this)

    }

    private fun initBanner() {
        MobileAds.initialize(this) { }
    }

    override fun onBackPressed() {
        COUNTER_INTER_AD++
        if (AdsConstants.LOAD_AD && COUNTER_INTER_AD.mod(TIMES_INTER_AD) == 0L)
            AdsConstants.showInter(this)
        else
            super.onBackPressed()
    }

    override fun close() {
        finish()
    }

    override fun show() {

    }

    override fun error() {
        finish()
    }


}