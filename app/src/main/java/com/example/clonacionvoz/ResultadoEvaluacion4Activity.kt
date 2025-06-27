package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoEvaluacion4Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_evaluacion4)

        val tvResultado = findViewById<TextView>(R.id.tvResultadoFinal4)
        val tvRetroalimentacion = findViewById<TextView>(R.id.tvRetroalimentacionFinal4)
        val tvProgreso = findViewById<TextView>(R.id.tvProgreso4)
        val imgResultado = findViewById<ImageView>(R.id.imgResultado4)
        val btnVolverMenu = findViewById<Button>(R.id.btnVolverMenu4)

        val puntaje = intent.getIntExtra("PUNTAJE", 0)

        tvResultado.text = "Tu puntaje es: $puntaje de 6"
        tvProgreso.text = "✅ Tu progreso ha sido guardado correctamente."

        val mensaje = when (puntaje) {
            in 0..2 -> {
                imgResultado.setImageResource(R.drawable.resultado_bajo)
                "Revisa nuevamente la Lección 4. Aprender a actuar ante una llamada sospechosa es clave para evitar fraudes."
            }
            in 3..4 -> {
                imgResultado.setImageResource(R.drawable.resultado_medio)
                "Lo estás haciendo bien. Refuerza tus conocimientos para actuar con más seguridad en llamadas sospechosas."
            }
            in 5..6 -> {
                imgResultado.setImageResource(R.drawable.resultado_alto)
                "¡Muy bien! Has aprendido cómo responder ante llamadas extrañas. Estás más preparado para evitar estafas."
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
