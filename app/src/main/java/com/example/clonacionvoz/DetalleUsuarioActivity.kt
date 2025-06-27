package com.example.clonacionvoz

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DetalleUsuarioActivity : AppCompatActivity() {

    private lateinit var recyclerHistorial: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private lateinit var btnBanear: Button
    private lateinit var btnDesbanear: Button
    private lateinit var tvNombre: TextView
    private lateinit var tvEstado: TextView
    private var tipoUsuario: String? = null
    private var estaBaneado: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_usuario)

        recyclerHistorial = findViewById(R.id.recyclerHistorial)
        recyclerHistorial.layoutManager = LinearLayoutManager(this)

        btnBanear = findViewById(R.id.btnBanear)
        btnDesbanear = findViewById(R.id.btnDesbanear)
        tvNombre = findViewById(R.id.tvNombreUsuario)
        tvEstado = findViewById(R.id.tvEstado)

        val uidUsuario = intent.getStringExtra("uidUsuario")

        if (uidUsuario == null) {
            Toast.makeText(this, "Error: Usuario no especificado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        db.collection("usuarios").document(uidUsuario).get()
            .addOnSuccessListener { doc ->
                val nombre = doc.getString("nombre") ?: ""
                val apellido = doc.getString("apellido") ?: ""
                tipoUsuario = doc.getString("tipoUsuario") ?: "usuario"
                estaBaneado = doc.getBoolean("baneado") ?: false

                tvNombre.text = "$nombre $apellido"
                tvEstado.text = if (estaBaneado) "Estado: 🚫 BANEADO" else "Estado: ✅ Activo"

                if (tipoUsuario != "administrador") {
                    btnBanear.visibility = View.VISIBLE
                    btnDesbanear.visibility = View.VISIBLE
                }

                btnBanear.setOnClickListener {
                    mostrarDialogoBanear(uidUsuario, true)
                }

                btnDesbanear.setOnClickListener {
                    mostrarDialogoBanear(uidUsuario, false)
                }
            }

        cargarHistorial(uidUsuario)
    }

    private fun mostrarDialogoBanear(uid: String, banear: Boolean) {
        val accion = if (banear) "banear" else "desbanear"
        val mensaje = if (banear) "¿Estás seguro de que deseas banear a este usuario?" else "¿Deseas quitarle el baneo a este usuario?"

        AlertDialog.Builder(this)
            .setTitle("Confirmar acción")
            .setMessage(mensaje)
            .setPositiveButton("Sí") { _, _ ->
                db.collection("usuarios").document(uid).update("baneado", banear)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Usuario $accion exitosamente", Toast.LENGTH_SHORT).show()
                        recreate()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al intentar $accion", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cargarHistorial(uidUsuario: String) {
        db.collection("evaluaciones")
            .whereEqualTo("uid", uidUsuario)
            .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val historialPorLeccion = mutableMapOf<Int, MutableList<HistorialItem.Evaluacion>>()

                for (doc in result) {
                    val leccion = doc.getLong("leccion")?.toInt() ?: continue
                    val puntaje = doc.getLong("puntaje")?.toInt() ?: 0
                    val total = doc.getLong("totalPreguntas")?.toInt() ?: 9
                    val fecha = doc.getTimestamp("fecha")?.toDate()?.let {
                        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(it)
                    } ?: "Fecha no disponible"

                    val item = HistorialItem.Evaluacion(fecha, puntaje, total)
                    historialPorLeccion.getOrPut(leccion) { mutableListOf() }.add(item)
                }

                val items = mutableListOf<HistorialItem>()
                historialPorLeccion.toSortedMap().forEach { (leccion, evaluaciones) ->
                    items.add(HistorialItem.Encabezado(leccion))

                    // Calcular promedio por lección
                    val promedio = if (evaluaciones.isNotEmpty()) {
                        evaluaciones.sumOf { it.puntaje } / evaluaciones.size
                    } else 0
                    items.add(HistorialItem.TextoExtra("Promedio: $promedio/9"))

                    // Ya vienen ordenadas por fecha DESC
                    evaluaciones.forEach { item -> items.add(item) }
                }

                if (items.isEmpty()) {
                    Toast.makeText(this, "Este usuario no ha realizado evaluaciones.", Toast.LENGTH_SHORT).show()
                }

                recyclerHistorial.adapter = HistorialAdapter(items)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar historial", Toast.LENGTH_SHORT).show()
            }
    }
}






