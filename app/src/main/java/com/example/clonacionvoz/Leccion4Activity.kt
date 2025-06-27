package com.example.clonacionvoz

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Leccion4Activity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leccion4)

        val btnAudio = findViewById<Button>(R.id.btnReproducirAudio)
        btnAudio.setOnClickListener {
            if (!isPlaying) {
                mediaPlayer = MediaPlayer.create(this, R.raw.leccion4_audio)
                mediaPlayer?.start()
                isPlaying = true
                btnAudio.text = "⏸️ Pausar audio"

                mediaPlayer?.setOnCompletionListener {
                    isPlaying = false
                    btnAudio.text = "🔊 Escuchar esta lección"
                }
            } else {
                mediaPlayer?.pause()
                isPlaying = false
                btnAudio.text = "🔊 Escuchar esta lección"
            }
        }

        val btnIrSimulacion = findViewById<Button>(R.id.btnIrSimulacion4)
        btnIrSimulacion.setOnClickListener {
            val intent = Intent(this, SimulacionLeccion4Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
