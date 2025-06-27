package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoEvaluacion3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_evaluacion3)

        val tvResultado = findViewById<TextView>(R.id.tvResultadoFinal3)
        val tvRetroalimentacion = findViewById<TextView>(R.id.tvRetroalimentacionFinal3)
        val tvProgreso = findViewById<TextView>(R.id.tvProgreso3)
        val imgResultado = findViewById<ImageView>(R.id.imgResultado3)
        val btnVolverMenu = findViewById<Button>(R.id.btnVolverMenu3)

        val puntaje = intent.getIntExtra("PUNTAJE", 0)

        tvResultado.text = "Tu puntaje es: $puntaje de 6"
        tvProgreso.text = "✅ Tu progreso ha sido guardado correctamente."

        val mensaje = when (puntaje) {
            in 0..2 -> {
                imgResultado.setImageResource(R.drawable.resultado_bajo)
                "Te sugerimos volver a revisar la Lección 3. Identificar señales de voz clonada puede protegerte de fraudes peligrosos."
            }
            in 3..4 -> {
                imgResultado.setImageResource(R.drawable.resultado_medio)
                "Vas por buen camino. Refuerza las señales clave para detectar una estafa con voz clonada y mantente alerta."
            }
            in 5..6 -> {
                imgResultado.setImageResource(R.drawable.resultado_alto)
                "¡Excelente! Has aprendido a detectar señales comunes de estafas con voz clonada. Puedes avanzar con confianza."
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
