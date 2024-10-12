package com.vicksam.ferapp.fer
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vicksam.ferapp.MainActivity
import husaynhakeem.io.facedetector.FaceBounds


class FerViewModel : ViewModel() {

    private val emotionLabels = MutableLiveData<Map<Int, String>>()
    fun emotionLabels(): LiveData<Map<Int, String>> = emotionLabels

    private var processing: Boolean = false

    fun onFacesDetected(
        context: Context,
        faceBounds: List<FaceBounds>,
        faceBitmaps: List<Bitmap>
    ) {
        if (faceBitmaps.isEmpty()) return

        synchronized(FerViewModel::class.java) {
            if (!processing) {
                processing = true
                Handler(Looper.getMainLooper()).post {
                    emotionLabels.value = faceBounds.mapNotNull { it.id }
                        .zip(faceBitmaps)
                        .toMap()
                        .run { getEmotionsMap(context ,this) }
                    processing = false
                }
            }
        }
    }

    /**
     * Given map of (faceId, faceBitmap), runs prediction on the model and
     * returns a map of (faceId, emotionLabel)
     */
    private fun getEmotionsMap(context: Context, faceImages: Map<Int, Bitmap>): Map<Int, String> {
        val emotionLabels = faceImages.map { FerModel.classify(it.value,context ) }
        return faceImages.keys.zip(emotionLabels).toMap()
    }
}