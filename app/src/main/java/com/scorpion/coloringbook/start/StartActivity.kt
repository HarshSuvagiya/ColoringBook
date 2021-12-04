package com.scorpion.coloringbook.start

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.category.CategoryActivity
import com.scorpion.coloringbook.collections.MyCollectionsActivity
import com.scorpion.coloringbook.databinding.ActivityStartBinding

import com.scorpion.coloringbook.base.BaseActivity
import com.scorpion.coloringbook.constants.AdsConstants
import com.scorpion.coloringbook.constants.Constants
import com.scorpion.coloringbook.exit.ExitActivity
import com.scorpion.coloringbook.extensions.Extensions.Companion.onClickThrottled


class StartActivity : BaseActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {

        Constants.SCREEN_WIDTH = (Constants.getScreenWidth(windowManager.defaultDisplay) / 2.2).toInt()

        binding.share.onClickThrottled {
            shareApp()
        }
        binding.privacy.onClickThrottled {
            privacyApp()
        }
        binding.start.onClickThrottled {
            openCategoryActivity()
        }
        binding.myCollections.onClickThrottled {
            openMyCollections()
        }

        loadAd()

    }

    private fun loadAd() {
        AdsConstants.loadBanner(windowManager.defaultDisplay, binding.adViewContainer, this)
    }

    private fun openMyCollections() {
        startActivity(Intent(applicationContext, MyCollectionsActivity::class.java))
    }

    private fun openCategoryActivity() {
        startActivity(Intent(applicationContext, CategoryActivity::class.java))
    }

    private fun privacyApp() {
        if (isNetworkAvailable()) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy)))
            startActivity(browserIntent)
        } else {
            Toast.makeText(applicationContext, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareApp() {
        try {
            val intent = Intent("android.intent.action.SEND")
            intent.type = "text/plain"
            intent.putExtra("android.intent.extra.SUBJECT", "Have a look at " + getString(R.string.app_name) + " app ")
            intent.putExtra(
                "android.intent.extra.TEXT",
                """Hey Check out this new ${getString(R.string.app_name)} App - ${getString(R.string.app_name)} Available on Google play store,You can also download it from "https://play.google.com/store/apps/details?id=$packageName""""
            )
            startActivity(Intent.createChooser(intent, "Share via"))
        } catch (e: Exception) {
        }
    }

    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun rateApp() {
        try {
            startActivity(
                Intent(
                    "android.intent.action.VIEW",
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (e: Exception) {
            startActivity(
                Intent(
                    "android.intent.action.VIEW",
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, ExitActivity::class.java))
        finish()
    }
}
