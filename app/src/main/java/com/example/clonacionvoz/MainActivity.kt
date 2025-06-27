package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Botones principales
        val btnLecciones = findViewById<Button>(R.id.btnLecciones)
        val btnSimulaciones = findViewById<Button>(R.id.btnSimulaciones)
        val btnEvaluacion = findViewById<Button>(R.id.btnEvaluacion)
        val btnProgreso = findViewById<Button>(R.id.btnProgreso)

        btnLecciones.setOnClickListener {
            startActivity(Intent(this, MenuLeccionesActivity::class.java))
        }
        btnSimulaciones.setOnClickListener {
            startActivity(Intent(this, SimulacionesActivity::class.java))
        }
        btnEvaluacion.setOnClickListener {
            startActivity(Intent(this, EvaluacionActivity::class.java))
        }
        btnProgreso.setOnClickListener {
            startActivity(Intent(this, ProgresoActivity::class.java))
        }

        // Mostrar nombre del usuario
        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        val uid = auth.currentUser?.uid

        if (uid != null) {
            db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val nombre = doc.getString("nombre") ?: ""
                        val apellido = doc.getString("apellido") ?: ""
                        tvSaludo.text = "¡Hola, $nombre $apellido!"
                    } else {
                        tvSaludo.text = "¡Hola, usuario!"
                    }
                }
                .addOnFailureListener {
                    tvSaludo.text = "¡Hola!"
                }
        }

        // Cerrar sesión
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}



