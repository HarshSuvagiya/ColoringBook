package com.scorpioninfosoft.coloringbook.exit

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.scorpioninfosoft.coloringbook.base.BaseActivity
import com.scorpioninfosoft.coloringbook.constants.AdsConstants
import com.scorpioninfosoft.coloringbook.databinding.ActivityExitBinding
import com.scorpioninfosoft.coloringbook.start.StartActivity

class ExitActivity : BaseActivity() {

    private lateinit var binding: ActivityExitBinding
    var currentNativeAd: UnifiedNativeAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        if (AdsConstants.LOAD_AD)
            loadNative()
        else
            binding.nativeAd.mainLayout.visibility = View.GONE
    }

    private fun loadNative() {
        AdsConstants.initNativeAdvanceAds(applicationContext, binding)
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, StartActivity::class.java))
        finish()
    }


}