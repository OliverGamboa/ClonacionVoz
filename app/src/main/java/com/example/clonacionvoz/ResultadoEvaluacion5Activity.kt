package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoEvaluacion5Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_evaluacion5)

        val tvResultado = findViewById<TextView>(R.id.tvResultadoFinal5)
        val tvRetroalimentacion = findViewById<TextView>(R.id.tvRetroalimentacionFinal5)
        val tvProgreso = findViewById<TextView>(R.id.tvProgreso5)
        val imgResultado = findViewById<ImageView>(R.id.imgResultado5)
        val btnVolverMenu = findViewById<Button>(R.id.btnVolverMenu5)

        val puntaje = intent.getIntExtra("PUNTAJE", 0)

        tvResultado.text = "Tu puntaje es: $puntaje de 5"
        tvProgreso.text = "✅ Tu progreso ha sido guardado correctamente."

        val mensaje = when (puntaje) {
            in 0..2 -> {
                imgResultado.setImageResource(R.drawable.resultado_bajo)
                "Te recomendamos estudiar la Lección 5 nuevamente. Proteger tu voz es esencial para evitar fraudes con IA."
            }
            in 3..4 -> {
                imgResultado.setImageResource(R.drawable.resultado_medio)
                "Vas bien. Revisa cómo identificar apps sospechosas para protegerte mejor."
            }
            5 -> {
                imgResultado.setImageResource(R.drawable.resultado_alto)
                "¡Excelente! Comprendiste cómo evitar fraudes por clonación de voz. Estás bien protegido."
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
