package com.vicksam.ferapp

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vicksam.ferapp.BaseDeDatos.DB_Estados

class ViewEmotionsActivity : AppCompatActivity() {

    private lateinit var dbEstados: DB_Estados
    private lateinit var textViewEmotions: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(com.vicksam.ferapp.R.layout.activity_view_emotions)

        //textViewEmotions = findViewById(R.id.textViewEmotions)
        dbEstados = DB_Estados(this)

        displayEmotions()
    }

    private fun displayEmotions() {
        val cursor: Cursor = dbEstados.getAllEmotions()
        val stringBuilder = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val time = cursor.getInt(cursor.getColumnIndexOrThrow("tiempo"))
                val frames = cursor.getInt(cursor.getColumnIndexOrThrow("fotogramas"))
                val neutral = cursor.getInt(cursor.getColumnIndexOrThrow("estado_neutral"))
                val happy = cursor.getInt(cursor.getColumnIndexOrThrow("estado_happy"))
                val surprised = cursor.getInt(cursor.getColumnIndexOrThrow("estado_surprised"))
                val sad = cursor.getInt(cursor.getColumnIndexOrThrow("estado_sad"))
                val anger = cursor.getInt(cursor.getColumnIndexOrThrow("estado_anger"))
                val disgust = cursor.getInt(cursor.getColumnIndexOrThrow("estado_disgust"))
                val fear = cursor.getInt(cursor.getColumnIndexOrThrow("estado_fear"))
                val contempt = cursor.getInt(cursor.getColumnIndexOrThrow("estado_contempt"))

                stringBuilder.append("ID: $id\nTime: $time seconds\nFrames: $frames\n")
                stringBuilder.append("Neutral: $neutral\nHappy: $happy\nSurprised: $surprised\n")
                stringBuilder.append("Sad: $sad\nAnger: $anger\nDisgust: $disgust\nFear: $fear\nContempt: $contempt\n\n")
                Log.d("ViewEmotionsActivity", "ID: $id, Time: $time, Frames: $frames, Neutral: $neutral, Happy: $happy, Surprised: $surprised, Sad: $sad, Anger: $anger, Disgust: $disgust, Fear: $fear, Contempt: $contempt")
            } while (cursor.moveToNext())
        }

        textViewEmotions.text = stringBuilder.toString()
        cursor.close()
    }
}