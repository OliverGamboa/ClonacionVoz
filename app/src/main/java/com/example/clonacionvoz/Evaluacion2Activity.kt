package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

class Evaluacion2Activity : AppCompatActivity() {

    private var respuestasCorrectas = 0
    private var preguntasContestadas = BooleanArray(7) { false }
    private var evaluacionTerminada = false

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluacion2)

        // Bloquear botón de retroceso para evitar pérdida de progreso
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!evaluacionTerminada) {
                    val dialog = AlertDialog.Builder(this@Evaluacion2Activity)
                        .setTitle("⚠️ Advertencia")
                        .setMessage("Si sale ahora, perderá todo su progreso y\nla evaluación no será guardada.\n\n¿Desea salir de todos modos?")
                        .setCancelable(false)
                        .setPositiveButton("Salir de todos modos") { _, _ ->
                            finish()
                        }
                        .setNegativeButton("Seguir evaluando") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()

                    dialog.show()

                    val messageView = dialog.findViewById<TextView>(android.R.id.message)
                    messageView?.textSize = 22f
                    messageView?.setTextColor(resources.getColor(android.R.color.white))
                    messageView?.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.textSize = 20f
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(android.R.color.holo_red_light))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.textSize = 20f
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(android.R.color.holo_green_light))
                } else {
                    finish()
                }
            }
        })

        val preguntas = listOf(
            Triple(R.id.rgPregunta1, R.id.rb1a, R.id.feedback1),
            Triple(R.id.rgPregunta2, R.id.rb2b, R.id.feedback2),
            Triple(R.id.rgPregunta3, R.id.rb3c, R.id.feedback3),
            Triple(R.id.rgPregunta4, R.id.rb4b, R.id.feedback4),
            Triple(R.id.rgPregunta5, R.id.rb5b, R.id.feedback5),
            Triple(R.id.rgPregunta6, R.id.rb6c, R.id.feedback6),
            Triple(R.id.rgPregunta7, R.id.rb7c, R.id.feedback7)
        )

        preguntas.forEachIndexed { index, (rgId, rbCorrectoId, feedbackId) ->
            val radioGroup = findViewById<RadioGroup>(rgId)
            val radioCorrecto = findViewById<RadioButton>(rbCorrectoId)
            val feedback = findViewById<TextView>(feedbackId)

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                if (!preguntasContestadas[index]) {
                    preguntasContestadas[index] = true
                    val selected = findViewById<RadioButton>(checkedId)

                    if (checkedId == rbCorrectoId) {
                        respuestasCorrectas++
                        feedback.text = "✅ ¡Correcto!"
                    } else {
                        feedback.text = "❌ Incorrecto. La respuesta correcta era:\n\"${radioCorrecto.text}\""
                    }
                }
            }
        }

        val btnFinalizar = findViewById<Button>(R.id.btnFinalizar2)
        btnFinalizar.setOnClickListener {
            if (preguntasContestadas.contains(false)) {
                Toast.makeText(this, "Por favor responde todas las preguntas.", Toast.LENGTH_LONG).show()
            } else {
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    val evaluacion = hashMapOf(
                        "uid" to uid,
                        "leccion" to 2,
                        "puntaje" to respuestasCorrectas,
                        "totalPreguntas" to preguntas.size,
                        "fecha" to Timestamp.now()
                    )

                    db.collection("evaluaciones")
                        .add(evaluacion)
                        .addOnSuccessListener {
                            evaluacionTerminada = true
                            val intent = Intent(this, ResultadoEvaluacion2Activity::class.java)
                            intent.putExtra("PUNTAJE", respuestasCorrectas)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al guardar la evaluación.", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}


