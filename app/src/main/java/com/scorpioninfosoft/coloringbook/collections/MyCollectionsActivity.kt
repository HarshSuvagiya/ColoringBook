package com.scorpioninfosoft.coloringbook.collections

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.MobileAds
import com.scorpioninfosoft.coloringbook.base.BaseActivity
import com.scorpioninfosoft.coloringbook.constants.AdsConstants
import com.scorpioninfosoft.coloringbook.databinding.ActivityMyCollectionsBinding

class MyCollectionsActivity : BaseActivity() {

    private lateinit var binding: ActivityMyCollectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyCollectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        loadAd()
    }

    private fun loadAd() {
        AdsConstants.loadBanner(windowManager.defaultDisplay, binding.adViewContainer, this)
    }

}