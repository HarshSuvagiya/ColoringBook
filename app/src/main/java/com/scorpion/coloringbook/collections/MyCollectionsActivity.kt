package com.scorpion.coloringbook.collections

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.base.BaseActivity
import com.scorpion.coloringbook.collections.adapter.ImageAdapter
import com.scorpion.coloringbook.constants.AdsConstants
import com.scorpion.coloringbook.constants.FileConstants
import com.scorpion.coloringbook.databinding.ActivityMyCollectionsBinding
import com.scorpion.coloringbook.extensions.Extensions.Companion.onClickThrottled
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class MyCollectionsActivity : BaseActivity() {

    private lateinit var binding: ActivityMyCollectionsBinding
    var fileArrayList = ArrayList<File>()
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyCollectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        loadAd()
        binding.topbar.back.onClickThrottled {
            onBackPressed()
        }

        binding.topbar.title.text = getString(R.string.collections)

    }

    override fun onResume() {
        super.onResume()
        getAllData()
    }

    private fun getAllData() {
        GlobalScope.launch {
            getImages()
        }
        setAdapter()
    }

    private fun setAdapter() {
        imageAdapter = ImageAdapter(fileArrayList)
        binding.imageRecyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
        binding.imageRecyclerView.adapter = imageAdapter
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