package com.scorpion.coloringbook.imagepreview

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.viewpager.widget.ViewPager
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.base.BaseActivity
import com.scorpion.coloringbook.collections.adapter.ImageViewPagerAdapter
import com.scorpion.coloringbook.constants.AdsConstants
import com.scorpion.coloringbook.constants.FileConstants
import com.scorpion.coloringbook.databinding.ActivityImagePreviewBinding
import com.scorpion.coloringbook.extensions.Extensions.Companion.onClickThrottled
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class ImagePreviewActivity : BaseActivity() {

    private lateinit var binding: ActivityImagePreviewBinding
    var finalPosition: Int = 0
    var fileArrayList = ArrayList<File>()
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {

        finalPosition = intent.getIntExtra("position", 0)

        loadAd()
        getAllData()
        binding.back.onClickThrottled {
            onBackPressed()
        }

        binding.screenShotViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                finalPosition = position
            }

            override fun onPageSelected(position: Int) {

            }

        })

        binding.share.onClickThrottled {
            var path = fileArrayList[finalPosition].absolutePath
            share(path)
        }

        binding.delete.onClickThrottled {
            showDeleteDialog()
        }

    }

    private fun delete(dialog: Dialog) {
        val file = File(fileArrayList[finalPosition].absolutePath)
        if (file.exists()) {
            if (file.delete()) {
                onBackPressed()
            } else {
                dialog.cancel()
            }
        }
    }

    private fun showDeleteDialog() {
        var dialog = Dialog(this)
        dialog.setContentView(R.layout.delete_popup)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val cancel = dialog.findViewById<ImageView>(R.id.cancel)
        val yes = dialog.findViewById<ImageView>(R.id.yes)
        val no = dialog.findViewById<ImageView>(R.id.no)

        cancel.onClickThrottled {
            dialog.dismiss()
        }
        yes.onClickThrottled {
            delete(dialog)
            dialog.dismiss()
        }
        no.onClickThrottled {
            dialog.dismiss()
        }

    }

    private fun share(path: String) {
        val share = Intent("android.intent.action.SEND")
        share.type = "image/*"
        val pathUri = FileProvider.getUriForFile(
            applicationContext,
            "$packageName.provider",
            File(path)
        )
        share.putExtra("android.intent.extra.STREAM", pathUri)
        startActivity(Intent.createChooser(share, "Share"))
    }

    private fun getAllData() {
        GlobalScope.launch {
            getImages()
            setAdapter()
        }
    }

    private fun setAdapter() {
        imageViewPagerAdapter = ImageViewPagerAdapter(fileArrayList)
        binding.screenShotViewPager.adapter = imageViewPagerAdapter
        binding.screenShotViewPager.currentItem = finalPosition
    }

    private fun loadAd() {
        AdsConstants.loadBanner(windowManager.defaultDisplay, binding.adViewContainer, this)
    }

    private fun getImages() {
        lateinit var listFiles: Array<File>
        fileArrayList.clear()
        val file = FileConstants.getRootDirPath(applicationContext)
        if (file.exists()) {
            listFiles = file.listFiles()
            if (listFiles != null) {
                for (files in listFiles) {
                    fileArrayList.add(files)
                }
            }
        }
        fileArrayList.sortWith { o1, o2 ->
            val s11 = o1?.lastModified()
            val s22 = o2?.lastModified()
            s22!!.compareTo(s11!!)
        }
    }

}