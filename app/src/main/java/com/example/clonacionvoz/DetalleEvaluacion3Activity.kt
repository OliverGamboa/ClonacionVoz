package com.example.clonacionvoz

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class DetalleEvaluacion3Activity : AppCompatActivity() {

    private lateinit var layoutIntentos: TableLayout
    private lateinit var chart: LineChart
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_evaluacion3)

        layoutIntentos = findViewById(R.id.layoutIntentos)
        chart = findViewById(R.id.chartUltimosIntentos)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val usuarioId = auth.currentUser?.uid ?: return

        db.collection("evaluaciones")
            .whereEqualTo("uid", usuarioId)
            .whereEqualTo("leccion", 3)
            .orderBy("fecha", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documentos ->

                if (documentos.isEmpty) {
                    Toast.makeText(this, "No hay intentos registrados para esta evaluación.", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                val entradasGrafico = mutableListOf<Entry>()
                val docs = documentos.documents.takeLast(5)
                var contador = documentos.size() - docs.size + 1

                for (doc in docs) {
                    val fecha = doc.getTimestamp("fecha")?.toDate()
                    val puntaje = doc.getLong("puntaje")?.toInt() ?: 0

                    entradasGrafico.add(Entry(contador.toFloat(), puntaje.toFloat()))

                    val card = CardView(this).apply {
                        radius = 16f
                        cardElevation = 8f
                        useCompatPadding = true
                        setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(0, 12, 0, 12)
                        layoutParams = params
                    }

                    val contenedorHorizontal = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_VERTICAL
                        setPadding(16, 24, 16, 24)
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }

                    fun celdaBonita(t: String): TextView {
                        return TextView(this@DetalleEvaluacion3Activity).apply {
                            text = t
                            textSize = 22f
                            setTextColor(Color.parseColor("#4E342E"))
                            gravity = Gravity.CENTER
                            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        }
                    }

                    contenedorHorizontal.addView(celdaBonita(contador.toString()))
                    contenedorHorizontal.addView(celdaBonita(formatoFecha.format(fecha ?: Date())))
                    contenedorHorizontal.addView(celdaBonita(puntaje.toString()))

                    card.addView(contenedorHorizontal)
                    layoutIntentos.addView(card)

                    contador++
                }

                mostrarGrafico(entradasGrafico)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener evaluaciones: ", e)
                Toast.makeText(this, "Error al cargar historial", Toast.LENGTH_LONG).show()
            }
    }

    private fun mostrarGrafico(entradas: List<Entry>) {
        val dataSet = LineDataSet(entradas, "Puntaje").apply {
            color = Color.parseColor("#FF7043")
            valueTextSize = 18f
            circleRadius = 6f
            lineWidth = 3f
            setCircleColor(Color.parseColor("#4E342E"))
            valueTextColor = Color.BLACK
        }

        chart.data = LineData(dataSet)

        chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 9f
            textSize = 16f
        }

        chart.axisRight.isEnabled = false
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 16f
            granularity = 1f
        }

        chart.setTouchEnabled(false)
        chart.setPinchZoom(false)
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.setExtraOffsets(24f, 24f, 24f, 24f)
        chart.animateY(1500)
        chart.invalidate()
    }
}
