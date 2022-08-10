package com.ahmet.features.utils.helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory

object ImpUserImage {

    fun implementUserImage(imagePath: String?): Bitmap {
        return BitmapFactory.decodeFile(imagePath)
    }
}