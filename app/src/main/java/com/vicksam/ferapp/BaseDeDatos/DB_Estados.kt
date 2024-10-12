package com.vicksam.ferapp.BaseDeDatos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DB_Estados(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "estados.db"
        private const val DATABASE_VERSION = 4

        private const val TABLE_NAME = "Estados"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TIME = "tiempo"
        private const val COLUMN_FRAMES = "fotogramas"
        private const val COLUMN_NEUTRAL = "estado_neutral"
        private const val COLUMN_HAPPY = "estado_feliz"
        private const val COLUMN_SURPRISED = "estado_sorprendido"
        private const val COLUMN_SAD = "estado_triste"
        private const val COLUMN_ANGER = "estado_enojado"
        private const val COLUMN_DISGUST = "estado_disgustado"
        private const val COLUMN_FEAR = "estado_miedo"
        private const val COLUMN_CONTEMPT = "estado_desprecio"
        private const val COLUMN_POSITIVE = "estado_positivo"
        private const val COLUMN_NEGATIVE = "estado_negativo"
        private const val COLUMN_PORCENTAJE_ANSIEDAD_DEPRESION = "porcentaje_ansiedad_depresion"

        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TIME INTEGER, " +
                "$COLUMN_FRAMES INTEGER, " +
                "$COLUMN_NEUTRAL INTEGER, " +
                "$COLUMN_HAPPY INTEGER, " +
                "$COLUMN_SURPRISED INTEGER, " +
                "$COLUMN_SAD INTEGER, " +
                "$COLUMN_ANGER INTEGER, " +
                "$COLUMN_DISGUST INTEGER, " +
                "$COLUMN_FEAR INTEGER, " +
                "$COLUMN_CONTEMPT INTEGER, " +
                "$COLUMN_POSITIVE INTEGER, " +
                "$COLUMN_NEGATIVE INTEGER, " +
                "$COLUMN_PORCENTAJE_ANSIEDAD_DEPRESION REAL DEFAULT 0.0)"
    }

    var finalFrames: Double = 0.0
    var finalNeutral: Double = 0.0
    var finalHappy: Double = 0.0
    var finalSurprised: Double = 0.0
    var finalSad: Double = 0.0
    var finalAnger: Double = 0.0
    var finalDisgust: Double = 0.0
    var finalFear: Double = 0.0
    var finalContempt: Double = 0.0
    var finalPositive: Double = 0.0
    var finalNegative: Double = 0.0
    var finalTime: Double = 0.0
    var puntaje_ansiedad: Double = 0.0

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)  // Crear la tabla cuando se crea la base de datos por primera vez
    }
    //Se ejecuta cuando se actualiza la base de datos
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_POSITIVE INTEGER DEFAULT 0")
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_NEGATIVE INTEGER DEFAULT 0")
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_PORCENTAJE_ANSIEDAD_DEPRESION REAL DEFAULT 0.0")
        }
    }

    fun insertEmotion(state: String, startTime: Long) {
        val db = writableDatabase
        val values = ContentValues()
        val currentTime = System.currentTimeMillis()

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC LIMIT 1", null)
        var frames = 1
        var neutral = 0
        var happy = 0
        var surprised = 0
        var sad = 0
        var anger = 0
        var disgust = 0
        var fear = 0
        var contempt = 0

        if (cursor.moveToFirst()) {
            frames = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FRAMES)) + 1
            neutral = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NEUTRAL))
            happy = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HAPPY))
            surprised = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SURPRISED))
            sad = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SAD))
            anger = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ANGER))
            disgust = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DISGUST))
            fear = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FEAR))
            contempt = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTEMPT))
        }

        cursor.close()

        when (state) {
            "neutral" -> neutral++
            "happy" -> happy++
            "surprised" -> surprised++
            "sad" -> sad++
            "anger" -> anger++
            "disgust" -> disgust++
            "fear" -> fear++
            "contempt" -> contempt++
        }

        values.put(COLUMN_FRAMES, frames)
        values.put(COLUMN_NEUTRAL, neutral)
        values.put(COLUMN_HAPPY, happy)
        values.put(COLUMN_SURPRISED, surprised)
        values.put(COLUMN_SAD, sad)
        values.put(COLUMN_ANGER, anger)
        values.put(COLUMN_DISGUST, disgust)
        values.put(COLUMN_FEAR, fear)
        values.put(COLUMN_CONTEMPT, contempt)

        val diff = currentTime - startTime
        val seconds = diff / 1000
        values.put(COLUMN_TIME, seconds)

        val positive = happy + surprised + contempt
        val negative = sad + anger + disgust + fear
        values.put(COLUMN_POSITIVE, positive)
        values.put(COLUMN_NEGATIVE, negative)

        // Calcular el puntaje de ansiedad
        //puntaje_ansiedad = (neutral * 0) + (happy * -0.5) + (surprised * 0.1) + (sad * 0.4) + (anger * 0.2) + (disgust * 0.1) + (fear * 0.3) + (contempt * 0.1)
        //values.put(COLUMN_PORCENTAJE_ANSIEDAD_DEPRESION, puntaje_ansiedad)



        // Update final variables
        finalFrames = frames.toDouble()
        finalNeutral = neutral.toDouble()
        finalHappy = happy.toDouble()
        finalSurprised = surprised.toDouble()
        finalSad = sad.toDouble()
        finalAnger = anger.toDouble()
        finalDisgust = disgust.toDouble()
        finalFear = fear.toDouble()
        finalContempt = contempt.toDouble()
        finalPositive = positive.toDouble()
        finalNegative = negative.toDouble()
        finalTime = seconds.toDouble()
        /*
        //frecuencia de cada estado = estado / fotogramas
        finalNeutral /= finalFrames
        finalHappy /= finalFrames
        finalSurprised /= finalFrames
        finalSad /= finalFrames
        finalAnger /= finalFrames
        finalDisgust /= finalFrames
        finalFear /= finalFrames
        finalContempt /= finalFrames
        finalPositive /= finalFrames
        finalNegative /= finalFrames
        //Calcular el puntaje total de ansiedad
        puntaje_ansiedad = (finalNeutral*0) + (finalHappy*(-0.5)) + (finalSurprised*0.1) + (finalSad*0.4) + (finalAnger*0.2) + (finalDisgust*0.1) + (finalFear*0.3) + (finalContempt*0.1)
        */

        //Calculo solo con estados relevantes
        //frecuencia de estados relevantes, sad, anger, fear, disgust

        finalSad /= finalFrames
        finalAnger /= finalFrames
        finalDisgust /= finalFrames
        finalFear /= finalFrames
        puntaje_ansiedad = (finalSad*0.4) + (finalAnger*0.2) + (finalDisgust*0.1) + (finalFear*0.3)
        values.put(COLUMN_PORCENTAJE_ANSIEDAD_DEPRESION, puntaje_ansiedad)

        if (frames == 1) {
            db.insert(TABLE_NAME, null, values) // Insertar la primera fila
        } else {
            db.update(TABLE_NAME, values, "$COLUMN_ID = 1", null) // Actualizar la primera fila
        }
    }

    fun getAllEmotions(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
}