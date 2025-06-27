package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

class Evaluacion1Activity : AppCompatActivity() {

    private var respuestasCorrectas = 0
    private var preguntasContestadas = BooleanArray(9) { false }
    private var evaluacionTerminada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluacion1)

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val uid = auth.currentUser?.uid

        if (uid == null) {
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!evaluacionTerminada) {
                    val dialog = AlertDialog.Builder(this@Evaluacion1Activity)
                        .setTitle("⚠️ Advertencia")
                        .setMessage("Si sale ahora, perderá todo su progreso y\nla evaluación no será guardada.\n\n¿Desea salir de todos modos?")
                        .setCancelable(false)
                        .setPositiveButton("Salir de todos modos") { _, _ -> finish() }
                        .setNegativeButton("Seguir evaluando") { dialog, _ -> dialog.dismiss() }
                        .create()

                    dialog.show()
                    dialog.window?.setBackgroundDrawableResource(android.R.color.black)

                    dialog.findViewById<TextView>(android.R.id.message)?.apply {
                        textSize = 22f
                        setTextColor(resources.getColor(android.R.color.white))
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    }
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
                        textSize = 20f
                        setTextColor(resources.getColor(android.R.color.holo_red_light))
                    }
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.apply {
                        textSize = 20f
                        setTextColor(resources.getColor(android.R.color.holo_green_light))
                    }
                } else {
                    finish()
                }
            }
        })

        val preguntas = listOf(
            Triple(R.id.rgPregunta1, R.id.rb1d, R.id.feedback1),
            Triple(R.id.rgPregunta2, R.id.rb2b, R.id.feedback2),
            Triple(R.id.rgPregunta3, R.id.rb3b, R.id.feedback3),
            Triple(R.id.rgPregunta4, R.id.rb4b, R.id.feedback4),
            Triple(R.id.rgPregunta5, R.id.rb5d, R.id.feedback5),
            Triple(R.id.rgPregunta6, R.id.rb6b, R.id.feedback6),
            Triple(R.id.rgPregunta7, R.id.rb7b, R.id.feedback7),
            Triple(R.id.rgPregunta8, R.id.rb8b, R.id.feedback8),
            Triple(R.id.rgPregunta9, R.id.rb9b, R.id.feedback9)
        )

        val respuestasUsuario = mutableListOf<Map<String, Any>>()

        preguntas.forEachIndexed { index, (rgId, rbCorrectoId, feedbackId) ->
            val radioGroup = findViewById<RadioGroup>(rgId)
            val radioCorrecto = findViewById<RadioButton>(rbCorrectoId)
            val feedback = findViewById<TextView>(feedbackId)

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                if (!preguntasContestadas[index]) {
                    preguntasContestadas[index] = true
                    val seleccionado = findViewById<RadioButton>(checkedId)

                    val esCorrecto = checkedId == rbCorrectoId
                    if (esCorrecto) {
                        respuestasCorrectas++
                        feedback.text = "✅ ¡Correcto!"
                    } else {
                        feedback.text = "❌ Incorrecto. La respuesta correcta era:\n\"${radioCorrecto.text}\""
                    }

                    respuestasUsuario.add(
                        mapOf(
                            "pregunta" to radioCorrecto.text.toString(),
                            "respuestaSeleccionada" to seleccionado.text.toString(),
                            "correcta" to esCorrecto
                        )
                    )
                }
            }
        }

        val btnFinalizar = findViewById<Button>(R.id.btnFinalizar)
        btnFinalizar.setOnClickListener {
            if (preguntasContestadas.contains(false)) {
                Toast.makeText(this, "Por favor responde todas las preguntas.", Toast.LENGTH_LONG).show()
            } else {
                evaluacionTerminada = true

                val resultado = hashMapOf(
                    "uid" to FirebaseAuth.getInstance().currentUser?.uid,
                    "leccion" to 1,
                    "puntaje" to respuestasCorrectas,
                    "totalPreguntas" to preguntas.size,
                    "fecha" to Timestamp.now(),
                    "respuestas" to respuestasUsuario
                )


                db.collection("evaluaciones")
                    .add(resultado)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Resultado guardado correctamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ResultadoEvaluacion1Activity::class.java)
                        intent.putExtra("PUNTAJE", respuestasCorrectas)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar evaluación", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}



