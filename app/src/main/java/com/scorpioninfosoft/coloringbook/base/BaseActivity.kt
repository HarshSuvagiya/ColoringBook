package com.scorpioninfosoft.coloringbook.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.MobileAds
import com.scorpioninfosoft.coloringbook.R
import com.scorpioninfosoft.coloringbook.constants.AdsConstants
import com.scorpioninfosoft.coloringbook.constants.interfaces.AdInterface
import android.view.animation.AlphaAnimation


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
        if (AdsConstants.LOAD_AD)
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