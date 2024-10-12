package com.vicksam.ferapp.fer

import android.content.Context
import android.graphics.Bitmap
import com.vicksam.ferapp.BaseDeDatos.DB_Estados
import com.vicksam.ferapp.utils.BitmapUtils.toGrayscale
import com.vicksam.ferapp.utils.BitmapUtils.toGrayscaleByteBuffer
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.exp

private const val MODEL_FILE_NAME = "fer_model.tflite"
private const val LABELS_FILE_NAME = "fer_model.names"

private const val INPUT_IMAGE_WIDTH = 48
private const val INPUT_IMAGE_HEIGHT = 48

private const val N_CLASSES = 8

object FerModel {

    private lateinit var interpreter: Interpreter
    private lateinit var labels: ArrayList<String>
    private lateinit var db_Emociones: DB_Estados
    private var startTime: Long = 0

    fun load(context: Context): Interpreter {
        if (!this::interpreter.isInitialized) {
            interpreter = loadModelFromAssets(context)
            labels = loadLabelsFromAssets(context)
            db_Emociones = DB_Estados(context)
        }
        return interpreter
    }

    fun classify(inputImage: Bitmap, context: Context): String {
        if (startTime == 0L) {
            startTime = System.currentTimeMillis()
        }

        val input = Bitmap.createScaledBitmap(
            inputImage, INPUT_IMAGE_WIDTH, INPUT_IMAGE_HEIGHT, false
        ).toGrayscale().toGrayscaleByteBuffer()

        val prediction = predict(input).toPrediction().toLabel()
        db_Emociones.insertEmotion(prediction, startTime)
        return prediction
    }

    private fun loadModelFromAssets(context: Context): Interpreter {
        val model = context.assets.open(MODEL_FILE_NAME).readBytes()
        val buffer = ByteBuffer.allocateDirect(model.size).order(ByteOrder.nativeOrder())
        buffer.put(model)
        return Interpreter(buffer)
    }

    private fun loadLabelsFromAssets(context: Context): ArrayList<String> =
        ArrayList<String>().apply {
            context.assets.open(LABELS_FILE_NAME).bufferedReader().useLines { lines ->
                lines.forEach { add(it) }
            }
        }

    private fun predict(input: ByteBuffer): FloatArray {
        val outputByteBuffer = ByteBuffer
            .allocateDirect(N_CLASSES * 4)
            .order(ByteOrder.nativeOrder())

        interpreter.run(input, outputByteBuffer)

        val logits = FloatArray(N_CLASSES)
        val labelsBuffer = outputByteBuffer.run {
            rewind()
            asFloatBuffer()
        }
        labelsBuffer.get(logits)
        return logits
    }

    private fun FloatArray.toPrediction(): Int {
        val probabilities = this.map { exp(it.toDouble()) }
            .run { map { it / sum() } }
        return probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
    }

    private fun Int.toLabel() = labels[this]
}