package com.scorpion.coloringbook.editor

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.scorpion.coloringbook.base.BaseActivity
import com.scorpion.coloringbook.category.model.CategoryModel
import com.scorpion.coloringbook.constants.AdsConstants
import com.scorpion.coloringbook.constants.Constants
import com.scorpion.coloringbook.constants.IntentConstants.Companion.CAT_MODEL
import com.scorpion.coloringbook.extensions.Extensions.Companion.onClickThrottled
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.constants.FileConstants
import com.scorpion.coloringbook.databinding.ActivityEditorBinding
import com.scorpion.coloringbook.editor.adapter.ColorsAdapter
import com.scorpion.coloringbook.editor.interfaces.GetItemClick
import com.scorpion.coloringbook.editor.model.EditorData


class EditorActivity : BaseActivity(), GetItemClick {

    private lateinit var binding: ActivityEditorBinding
    private lateinit var model: CategoryModel
    private lateinit var bitmap: Bitmap
    private var colorList: ArrayList<EditorData> = ArrayList()
    private lateinit var colorsAdapter: ColorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        getIntentExtra()
        loadAd()
        initClicks()
        setAdapter()
        setListenerForStrokeWidth()
    }

    private fun setAdapter() {
        var colors = resources.getIntArray(R.array.material_colors)
        for (item in colors)
            colorList.add(EditorData(item))
        colorsAdapter = ColorsAdapter(colorList, this)
        binding.colorSliderRecycler.adapter = colorsAdapter
    }

    private fun setListenerForStrokeWidth() {
        binding.drawView.setColor(ContextCompat.getColor(applicationContext, R.color.red_50))
        binding.strokeWidth.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.drawView.setStrokeWidth(progress.toFloat())
                setSizeForActualStrokeIndicator(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private fun setSizeForActualStrokeIndicator(progress: Int) {
        var tmp = (progress * 1.5).toInt()
        val params = LinearLayout.LayoutParams(tmp, tmp)
        binding.actualStrokeIndicator.layoutParams = params
    }

    private fun initClicks() {
        binding.back.onClickThrottled {
            onBackPressed()
        }
        binding.eraser.onClickThrottled {
            binding.drawView.toggleEraser()
            updatePencil()
        }
        binding.undo.onClickThrottled {
            binding.drawView.undo()
        }
        binding.redo.onClickThrottled {
            binding.drawView.redo()
        }
        binding.delete.onClickThrottled {
            binding.drawView.clearCanvas()
        }
        binding.save.onClickThrottled {
            save()
        }
        binding.share.onClickThrottled {
            var path = FileConstants.getRootDirPath(applicationContext).absolutePath + File.separator + System.currentTimeMillis() + ".png"
            saveBitmap(path, createSingleImageFromMultipleImages(getBitmapFromView(binding.bg)!!, getBitmapFromView(binding.drawView)!!, getBitmapFromView(binding.image)!!)!!)
            share(path)
        }
    }

    private fun save() {
        var path = FileConstants.getRootDirPath(applicationContext).absolutePath + File.separator + System.currentTimeMillis() + ".png"
        saveBitmap(path, createSingleImageFromMultipleImages(getBitmapFromView(binding.bg)!!, getBitmapFromView(binding.drawView)!!, getBitmapFromView(binding.image)!!)!!)
    }

    open fun getBitmapFromView(view: View): Bitmap? {
        var bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun createSingleImageFromMultipleImages(firstImage: Bitmap, secondImage: Bitmap, thirdImage: Bitmap): Bitmap? {
        val result = Bitmap.createBitmap(firstImage.width, firstImage.height, firstImage.config)
        val canvas = Canvas(result)
        canvas.drawBitmap(firstImage, 0f, 0f, null)
        canvas.drawBitmap(secondImage, 30f, 30f, null)
        canvas.drawBitmap(thirdImage, 30f, 30f, null)
        return result
    }

    private fun getIntentExtra() {
        model = intent.getSerializableExtra(CAT_MODEL) as CategoryModel
        Glide.with(applicationContext).asBitmap().load(model.thumbUrl).into(object : BitmapImageViewTarget(binding.image) {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                super.onResourceReady(resource, transition)
                bitmap = resource
            }
        })
    }

    private fun loadAd() {
        AdsConstants.loadBanner(windowManager.defaultDisplay, binding.adViewContainer, this)
    }

    fun saveBitmap(file: String?, bitmap: Bitmap) {
        try {
            val ostream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream)
            ostream.close()
            Toast.makeText(applicationContext, getString(R.string.saved), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, getString(R.string.exe), Toast.LENGTH_SHORT).show()
        }
    }

    var lastPos = -1
    override fun onItemClick(item: EditorData, pos: Int) {
        if (binding.drawView.isEraserOn)
            binding.drawView.toggleEraser()
        binding.drawView.setColor(item.color)
        val drawable: Drawable = binding.strokeWidth.progressDrawable
        drawable.colorFilter = LightingColorFilter(-0x1000000, item.color)
        binding.strokeWidth.setProgressDrawableTiled(drawable)
        binding.actualStrokeIndicator.setBackgroundColor(item.color)
        colorsAdapter.list[pos].isSelected = true
        if (lastPos != -1)
            colorsAdapter.list[lastPos].isSelected = false
        lastPos = pos
        colorsAdapter.notifyDataSetChanged()
    }

    private fun updatePencil() {
        if (lastPos != -1)
            colorsAdapter.list[lastPos].isSelected = false
        colorsAdapter.notifyDataSetChanged()
    }

    fun share(path: String) {
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

}