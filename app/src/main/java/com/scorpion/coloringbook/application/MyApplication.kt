package com.scorpion.coloringbook.application

import android.app.Application
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.MobileAds




class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this) { }
    }

}