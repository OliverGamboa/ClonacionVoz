package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoEvaluacion1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_evaluacion1)

        val tvResultado = findViewById<TextView>(R.id.tvResultadoFinal)
        val tvRetroalimentacion = findViewById<TextView>(R.id.tvRetroalimentacionFinal)
        val tvProgreso = findViewById<TextView>(R.id.tvProgreso)
        val imgResultado = findViewById<ImageView>(R.id.imgResultado)
        val btnVolverMenu = findViewById<Button>(R.id.btnVolverMenu)

        val puntaje = intent.getIntExtra("PUNTAJE", 0)

        tvResultado.text = "Tu puntaje es: $puntaje de 9"
        tvProgreso.text = "✅ Tu progreso ha sido guardado correctamente."

        val mensaje = when (puntaje) {
            in 0..3 -> {
                imgResultado.setImageResource(R.drawable.resultado_bajo) // opcional
                "Te recomendamos volver a leer toda la lección para comprender mejor el tema. ¡No te preocupes, aprender es un proceso!"
            }
            in 4..7 -> {
                imgResultado.setImageResource(R.drawable.resultado_medio) // opcional
                "Has comprendido parte del contenido. Te sugerimos repasar la lección antes de continuar, aunque puedes avanzar si lo deseas."
            }
            in 8..9 -> {
                imgResultado.setImageResource(R.drawable.resultado_alto) // opcional
                "¡Excelente! Has entendido muy bien la lección. Puedes avanzar a la siguiente sin problema."
            }
            else -> "Resultado inesperado."
        }

        tvRetroalimentacion.text = mensaje

        btnVolverMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
