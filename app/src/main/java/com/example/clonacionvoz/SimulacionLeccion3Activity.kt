package com.example.clonacionvoz

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SimulacionLeccion3Activity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulacion_leccion3)

        val btnAudio = findViewById<Button>(R.id.btnReproducirAudio)
        btnAudio.setOnClickListener {
            if (!isPlaying) {
                mediaPlayer = MediaPlayer.create(this, R.raw.simulacion3_audio)
                mediaPlayer?.start()
                isPlaying = true
                btnAudio.text = "⏸️ Pausar llamada"

                mediaPlayer?.setOnCompletionListener {
                    isPlaying = false
                    btnAudio.text = "🔊 Reproducir llamada simulada"
                }
            } else {
                mediaPlayer?.pause()
                isPlaying = false
                btnAudio.text = "🔊 Reproducir llamada simulada"
            }
        }

        val btn1 = findViewById<Button>(R.id.btnOpcion1)
        val btn2 = findViewById<Button>(R.id.btnOpcion2)
        val btn3 = findViewById<Button>(R.id.btnOpcion3)

        val feedback1 = findViewById<TextView>(R.id.feedbackOpcion1)
        val feedback2 = findViewById<TextView>(R.id.feedbackOpcion2)
        val feedback3 = findViewById<TextView>(R.id.feedbackOpcion3)

        val btnEvaluacion = findViewById<Button>(R.id.btnIrEvaluacion3)
        btnEvaluacion.setOnClickListener {
            startActivity(Intent(this, Evaluacion3Activity::class.java))
        }

        btn1.setOnClickListener {
            colorearBoton(btn1, false)
            colorearBoton(btn2, false)
            colorearBoton(btn3, true)

            feedback1.text = "❌ No se debe confiar solo por el tono de voz."
            feedback2.text = ""
            feedback3.text = ""
            deshabilitarBotones(btn1, btn2, btn3)
        }

        btn2.setOnClickListener {
            colorearBoton(btn1, false)
            colorearBoton(btn2, false)
            colorearBoton(btn3, true)

            feedback1.text = ""
            feedback2.text = "❌ Aunque parece lógico, insistir puede no llevar a nada. Es mejor cortar y confirmar."
            feedback3.text = ""
            deshabilitarBotones(btn1, btn2, btn3)
        }

        btn3.setOnClickListener {
            colorearBoton(btn1, false)
            colorearBoton(btn2, false)
            colorearBoton(btn3, true)

            feedback1.text = ""
            feedback2.text = ""
            feedback3.text = "✅ Muy bien. Ante señales extrañas, se debe colgar y buscar confirmar por otro medio."
            deshabilitarBotones(btn1, btn2, btn3)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun deshabilitarBotones(vararg botones: Button) {
        botones.forEach { it.isEnabled = false }
    }

    private fun colorearBoton(boton: Button, esCorrecta: Boolean) {
        boton.backgroundTintList = ContextCompat.getColorStateList(this,
            if (esCorrecta) R.color.verde_correcto else R.color.rojo_incorrecto
        )
        boton.setTextColor(Color.WHITE)
    }
}

