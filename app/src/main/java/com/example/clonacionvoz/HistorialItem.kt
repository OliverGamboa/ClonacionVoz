package com.example.clonacionvoz

sealed class HistorialItem {
    data class Encabezado(val leccion: Int) : HistorialItem()
    data class Evaluacion(
        val fecha: String,
        val puntaje: Int,
        val total: Int
    ) : HistorialItem()
    data class TextoExtra(val contenido: String) : HistorialItem() // ← nuevo tipo para el promedio
}

