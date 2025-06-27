package com.example.clonacionvoz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistorialAdapter(private val items: List<HistorialItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TIPO_ENCABEZADO = 0
        const val TIPO_EVALUACION = 1
        const val TIPO_TEXTO_EXTRA = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HistorialItem.Encabezado -> TIPO_ENCABEZADO
            is HistorialItem.Evaluacion -> TIPO_EVALUACION
            is HistorialItem.TextoExtra -> TIPO_TEXTO_EXTRA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TIPO_ENCABEZADO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_encabezado_leccion, parent, false)
                EncabezadoViewHolder(view)
            }
            TIPO_TEXTO_EXTRA -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_texto_extra, parent, false)
                TextoExtraViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_evaluacion_historial, parent, false)
                EvaluacionViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is HistorialItem.Encabezado -> (holder as EncabezadoViewHolder).bind(item)
            is HistorialItem.Evaluacion -> (holder as EvaluacionViewHolder).bind(item)
            is HistorialItem.TextoExtra -> (holder as TextoExtraViewHolder).bind(item)
        }
    }

    override fun getItemCount() = items.size

    class EncabezadoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvLeccion = view.findViewById<TextView>(R.id.tvLeccionEncabezado)
        fun bind(item: HistorialItem.Encabezado) {
            tvLeccion.text = "📘 Lección ${item.leccion}"
        }
    }

    class EvaluacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvFecha = view.findViewById<TextView>(R.id.tvFecha)
        private val tvPuntaje = view.findViewById<TextView>(R.id.tvPuntaje)

        fun bind(item: HistorialItem.Evaluacion) {
            tvFecha.text = "📅 ${item.fecha}"
            tvPuntaje.text = "🟢 Puntaje: ${item.puntaje}/${item.total}"
        }
    }

    class TextoExtraViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvExtra = view.findViewById<TextView>(R.id.tvTextoExtra)
        fun bind(item: HistorialItem.TextoExtra) {
            tvExtra.text = item.contenido
        }
    }
}


