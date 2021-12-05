package com.scorpion.coloringbook.splash

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.constants.AdsConstants
import com.scorpion.coloringbook.constants.Constants.Companion.COUNT_DOWN_INTERVAL
import com.scorpion.coloringbook.constants.Constants.Companion.FINAL_COUNT_DOWN
import com.scorpion.coloringbook.constants.DBConstants.Companion.ADS
import com.scorpion.coloringbook.constants.DBConstants.Companion.APP_OPEN_ID
import com.scorpion.coloringbook.constants.DBConstants.Companion.BANNER_ID
import com.scorpion.coloringbook.constants.DBConstants.Companion.COLORING_BOOK
import com.scorpion.coloringbook.constants.DBConstants.Companion.INTER_ID
import com.scorpion.coloringbook.constants.DBConstants.Companion.LOAD_ID
import com.scorpion.coloringbook.constants.DBConstants.Companion.NATIVE_ID
import com.scorpion.coloringbook.constants.DBConstants.Companion.REWARD_ID
import com.scorpion.coloringbook.constants.DBConstants.Companion.TIMES_INTER_AD
import com.scorpion.coloringbook.constants.DBConstants.Companion.USE_TEST_ID
import com.scorpion.coloringbook.databinding.ActivitySplashBinding
import com.scorpion.coloringbook.extensions.Extensions.Companion.onClickThrottled
import com.scorpion.coloringbook.start.StartActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    var db = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        object : CountDownTimer(FINAL_COUNT_DOWN, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (isNetworkConnected()) {
                    db.child(COLORING_BOOK).child(ADS).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            AdsConstants.USE_TEST_AD_ID = snapshot.child(USE_TEST_ID).value as Boolean
                            AdsConstants.LOAD_AD = snapshot.child(LOAD_ID).value as Boolean
                            AdsConstants.TIMES_INTER_AD = snapshot.child(TIMES_INTER_AD).value as Long

                            if (!AdsConstants.USE_TEST_AD_ID) {
                                AdsConstants.BANNER_ID = snapshot.child(BANNER_ID).value.toString()
                                AdsConstants.INTER_ID = snapshot.child(INTER_ID).value.toString()
                                AdsConstants.NATIVE_ID = snapshot.child(NATIVE_ID).value.toString()
                                AdsConstants.APP_OPEN_ID = snapshot.child(APP_OPEN_ID).value.toString()
                                AdsConstants.REWARD_ID = snapshot.child(REWARD_ID).value.toString()

                                openStartActivity()
                            } else {
                                openStartActivity()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                } else {
                    showNoInternetDialog()
                }
            }
        }.start()
    }

    private fun showNoInternetDialog() {
        var dialog = Dialog(this)
        dialog.setContentView(R.layout.delete_popup)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val cancel = dialog.findViewById<ImageView>(R.id.cancel)
        val yes = dialog.findViewById<ImageView>(R.id.yes)
        val no = dialog.findViewById<ImageView>(R.id.no)
        val title = dialog.findViewById<TextView>(R.id.title)

        no.visibility = View.GONE
        title.text = getString(R.string.no_internet)

        cancel.onClickThrottled {
            dialog.dismiss()
            finishAffinity()
        }

        yes.onClickThrottled {
            dialog.dismiss()
            val i = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }

    }

    private fun openStartActivity() {
        startActivity(Intent(applicationContext, StartActivity::class.java))
        finish()
    }

    private fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
}