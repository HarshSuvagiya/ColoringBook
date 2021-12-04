package com.scorpion.coloringbook.constants

import android.content.Context
import com.scorpion.coloringbook.R
import java.io.File

class FileConstants {

    companion object {
        fun getRootDirPath(mContext: Context): File {
            var path = File(mContext.getExternalFilesDir("").toString() + File.separator + mContext.getString(R.string.app_name) + File.separator + mContext.resources.getString(R.string.images))
            if (!path.exists())
                path.mkdirs()
            return path
        }

    }
}