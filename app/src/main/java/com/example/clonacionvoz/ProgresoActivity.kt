package com.example.clonacionvoz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.github.mikephil.charting.formatter.ValueFormatter


class ProgresoActivity : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private val etiquetas = listOf("E1", "E2", "E3", "E4", "E5")
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val puntajesMap = mutableMapOf<Int, Int>() // leccion -> puntaje

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progreso)

        barChart = findViewById(R.id.barChartProgreso)
        val usuarioId = auth.currentUser?.uid

        if (usuarioId != null) {
            cargarPuntajesDesdeFirestore(usuarioId)
        }
    }

    private fun cargarPuntajesDesdeFirestore(usuarioId: String) {
        db.collection("evaluaciones")
            .whereEqualTo("uid", usuarioId)
            .orderBy("fecha", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documentos ->
                val registrados = mutableSetOf<Int>()

                for (doc in documentos) {
                    val leccion = doc.getLong("leccion")?.toInt() ?: continue
                    val puntaje = doc.getLong("puntaje")?.toInt() ?: 0

                    if (!registrados.contains(leccion)) {
                        puntajesMap[leccion] = puntaje
                        registrados.add(leccion)
                    }

                    if (registrados.size == 5) break
                }

                mostrarGrafico()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al leer evaluaciones", e)
            }
    }

    private fun mostrarGrafico() {
        val entradas = ArrayList<BarEntry>()
        val coloresBarras = listOf(
            Color.parseColor("#FF7043"), // E1 - Naranja
            Color.parseColor("#66BB6A"), // E2 - Verde
            Color.parseColor("#42A5F5"), // E3 - Azul
            Color.parseColor("#AB47BC"), // E4 - Púrpura
            Color.parseColor("#FFCA28")  // E5 - Amarillo
        )

        for (i in 1..5) {
            val puntaje = puntajesMap[i] ?: 0
            entradas.add(BarEntry((i - 1).toFloat(), puntaje.toFloat()))
        }

        val dataSet = BarDataSet(entradas, "Puntajes por Evaluación").apply {
            valueTextSize = 18f
            colors = coloresBarras
            valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    return barEntry?.y?.toInt()?.toString() ?: ""
                }
            }
        }

        val data = BarData(dataSet).apply {
            barWidth = 0.8f
        }

        barChart.data = data

        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(etiquetas)
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 18f
            granularity = 1f
            setDrawGridLines(false)
            setDrawAxisLine(true)
            textColor = Color.parseColor("#4E342E")
        }

        barChart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 9f
            granularity = 1f
            textSize = 16f
            setDrawGridLines(true)
            gridColor = Color.parseColor("#E0E0E0")
            textColor = Color.parseColor("#4E342E")
        }

        barChart.axisRight.isEnabled = false
        barChart.setDrawValueAboveBar(false)
        barChart.setPinchZoom(false)
        barChart.setScaleEnabled(false)
        barChart.setTouchEnabled(true)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setExtraOffsets(24f, 24f, 24f, 24f) // 🎯 margen interno controlado
        barChart.setBackgroundColor(Color.WHITE)    // 🎯 fondo blanco que sí funciona con CardView

        barChart.animateY(1200)
        barChart.invalidate()

        barChart.setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
            override fun onValueSelected(e: com.github.mikephil.charting.data.Entry?, h: com.github.mikephil.charting.highlight.Highlight?) {
                val index = e?.x?.toInt() ?: return
                val numeroEvaluacion = index + 1
                try {
                    val intent = Intent(this@ProgresoActivity, Class.forName("com.example.clonacionvoz.DetalleEvaluacion${numeroEvaluacion}Activity"))
                    startActivity(intent)
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }

            override fun onNothingSelected() {}
        })
    }

}








