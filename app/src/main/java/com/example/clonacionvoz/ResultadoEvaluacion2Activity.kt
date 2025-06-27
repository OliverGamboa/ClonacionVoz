package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoEvaluacion2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_evaluacion2)

        val tvResultado = findViewById<TextView>(R.id.tvResultadoFinal2)
        val tvRetroalimentacion = findViewById<TextView>(R.id.tvRetroalimentacionFinal2)
        val tvProgreso = findViewById<TextView>(R.id.tvProgreso2)
        val imgResultado = findViewById<ImageView>(R.id.imgResultado2)
        val btnVolverMenu = findViewById<Button>(R.id.btnVolverMenu2)

        val puntaje = intent.getIntExtra("PUNTAJE", 0)

        tvResultado.text = "Tu puntaje es: $puntaje de 7"
        tvProgreso.text = "✅ Tu progreso ha sido guardado correctamente."

        val mensaje = when (puntaje) {
            in 0..2 -> {
                imgResultado.setImageResource(R.drawable.resultado_bajo)
                "Te recomendamos revisar nuevamente la lección 2. Recuerda: las estafas pueden parecer inofensivas pero son peligrosas."
            }
            in 3..5 -> {
                imgResultado.setImageResource(R.drawable.resultado_medio)
                "Vas por buen camino. Refuerza los conceptos de la lección 2 para mayor seguridad ante posibles fraudes."
            }
            in 6..7 -> {
                imgResultado.setImageResource(R.drawable.resultado_alto)
                "¡Excelente trabajo! Has comprendido los riesgos de los fraudes por voz clonada en situaciones comunes. Puedes continuar."
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
