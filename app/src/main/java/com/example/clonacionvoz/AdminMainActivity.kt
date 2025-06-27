package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminMainActivity : AppCompatActivity() {

    private lateinit var btnVerUsuarios: Button
    private lateinit var btnVerProgreso: Button
    private lateinit var btnLecciones: Button
    private lateinit var btnSimulaciones: Button
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        btnVerUsuarios = findViewById(R.id.btnVerUsuarios)
        btnVerProgreso = findViewById(R.id.btnVerProgreso)
        btnLecciones = findViewById(R.id.btnLecciones)
        btnSimulaciones = findViewById(R.id.btnSimulaciones)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)

        btnVerUsuarios.setOnClickListener {
            startActivity(Intent(this, ListaUsuariosActivity::class.java))
        }

        btnVerProgreso.setOnClickListener {
            startActivity(Intent(this, ProgresoGlobalActivity::class.java))
        }

        btnLecciones.setOnClickListener {
            startActivity(Intent(this, MenuLeccionesActivity::class.java))
        }

        btnSimulaciones.setOnClickListener {
            startActivity(Intent(this, SimulacionesActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}

