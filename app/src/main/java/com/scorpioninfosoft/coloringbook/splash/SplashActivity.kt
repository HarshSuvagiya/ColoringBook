package com.scorpioninfosoft.coloringbook.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.scorpioninfosoft.coloringbook.constants.AdsConstants
import com.scorpioninfosoft.coloringbook.constants.Constants.Companion.COUNT_DOWN_INTERVAL
import com.scorpioninfosoft.coloringbook.constants.Constants.Companion.FINAL_COUNT_DOWN
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.ADS
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.APP_OPEN_ID
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.BANNER_ID
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.COLORING_BOOK
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.INTER_ID
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.LOAD_ID
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.NATIVE_ID
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.REWARD_ID
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.USE_TEST_ID
import com.scorpioninfosoft.coloringbook.databinding.ActivitySplashBinding
import com.scorpioninfosoft.coloringbook.start.StartActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding
    var db = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        object : CountDownTimer(FINAL_COUNT_DOWN,COUNT_DOWN_INTERVAL){
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                db.child(COLORING_BOOK).child(ADS).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        AdsConstants.USE_TEST_AD_ID = snapshot.child(USE_TEST_ID).value as Boolean
                        AdsConstants.LOAD_AD = snapshot.child(LOAD_ID).value as Boolean

                        if (!AdsConstants.USE_TEST_AD_ID){
                            AdsConstants.BANNER_ID = snapshot.child(BANNER_ID).value.toString()
                            AdsConstants.INTER_ID = snapshot.child(INTER_ID).value.toString()
                            AdsConstants.NATIVE_ID = snapshot.child(NATIVE_ID).value.toString()
                            AdsConstants.APP_OPEN_ID = snapshot.child(APP_OPEN_ID).value.toString()
                            AdsConstants.REWARD_ID = snapshot.child(REWARD_ID).value.toString()

                            openStartActivity()
                        }
                        else{
                            openStartActivity()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }.start()
    }

    private fun openStartActivity() {
        startActivity(Intent(applicationContext,StartActivity::class.java))
        finish()
    }
}