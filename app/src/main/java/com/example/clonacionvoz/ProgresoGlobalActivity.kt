package com.example.clonacionvoz

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.firestore.FirebaseFirestore

class ProgresoGlobalActivity : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private val etiquetas = listOf("E1", "E2", "E3", "E4", "E5")
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progreso_global)

        barChart = findViewById(R.id.barChartProgreso)

        db.collection("evaluaciones")
            .get()
            .addOnSuccessListener { result ->
                val puntajesPorLeccion = mutableMapOf<Int, MutableList<Int>>()

                for (doc in result) {
                    val leccion = doc.getLong("leccion")?.toInt() ?: continue
                    val puntaje = doc.getLong("puntaje")?.toInt() ?: 0

                    if (!puntajesPorLeccion.containsKey(leccion)) {
                        puntajesPorLeccion[leccion] = mutableListOf()
                    }
                    puntajesPorLeccion[leccion]?.add(puntaje)
                }

                mostrarGrafico(puntajesPorLeccion)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener evaluaciones", e)
            }
    }

    private fun mostrarGrafico(puntajesPorLeccion: Map<Int, List<Int>>) {
        val entradas = ArrayList<BarEntry>()
        val coloresBarras = listOf(
            Color.parseColor("#FF7043"), // E1 - Naranja
            Color.parseColor("#66BB6A"), // E2 - Verde
            Color.parseColor("#42A5F5"), // E3 - Azul
            Color.parseColor("#AB47BC"), // E4 - Púrpura
            Color.parseColor("#FFCA28")  // E5 - Amarillo
        )

        for (i in 1..5) {
            val lista = puntajesPorLeccion[i] ?: emptyList()
            val promedio = if (lista.isNotEmpty()) lista.average().toFloat() else 0f
            entradas.add(BarEntry((i - 1).toFloat(), promedio))
        }

        val dataSet = BarDataSet(entradas, "Promedio por Evaluación").apply {
            valueTextSize = 18f
            colors = coloresBarras
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
            labelCount = etiquetas.size
            isGranularityEnabled = true
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
        barChart.setTouchEnabled(false)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setExtraOffsets(24f, 24f, 24f, 24f)
        barChart.setBackgroundColor(Color.WHITE)

        barChart.animateY(1200)
        barChart.invalidate()
    }
}

