package com.scorpion.coloringbook.exit

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.scorpion.coloringbook.base.BaseActivity
import com.scorpion.coloringbook.constants.AdsConstants
import com.scorpion.coloringbook.databinding.ActivityExitBinding
import com.scorpion.coloringbook.extensions.Extensions.Companion.onClickThrottled
import com.scorpion.coloringbook.start.StartActivity

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

        binding.yes.onClickThrottled {
            finish()
        }
        binding.no.onClickThrottled {
            onBackPressed()
        }

    }

    private fun loadNative() {
        AdsConstants.initNativeAdvanceAds(applicationContext, binding)
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, StartActivity::class.java))
        finish()
    }


}