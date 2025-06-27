package com.example.clonacionvoz

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DetalleEvaluacionActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_evaluacion1)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser?.uid ?: return
        val leccion = intent.getIntExtra("leccion", -1)
        val layoutIntentos = findViewById<LinearLayout>(R.id.layoutIntentos)
        val tvTitulo = findViewById<TextView>(R.id.tvTituloHistorial)
        tvTitulo.text = "Historial de Evaluación - Lección $leccion"

        db.collection("evaluaciones")
            .whereEqualTo("uid", uid)
            .whereEqualTo("leccion", leccion)
            .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { docs ->
                if (!docs.isEmpty) {
                    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    var contador = 1
                    for (doc in docs) {
                        val puntaje = (doc["puntaje"] as Long).toInt()
                        val total = (doc["totalPreguntas"] as Long).toInt()
                        val fecha = (doc["fecha"] as com.google.firebase.Timestamp).toDate()

                        val texto = "Intento #$contador\n" +
                                "Fecha: ${formato.format(fecha)}\n" +
                                "Puntaje: $puntaje de $total"

                        val tv = TextView(this)
                        tv.text = texto
                        tv.textSize = 20f
                        tv.setTextColor(resources.getColor(android.R.color.black))
                        tv.setPadding(16, 16, 16, 16)

                        layoutIntentos.addView(tv)
                        contador++
                    }
                } else {
                    val tv = TextView(this)
                    tv.text = "No hay intentos registrados."
                    tv.textSize = 20f
                    tv.setTextColor(resources.getColor(android.R.color.darker_gray))
                    layoutIntentos.addView(tv)
                }
            }
    }
}
