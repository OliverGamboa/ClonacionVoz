package com.example.clonacionvoz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class UsuarioMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main)

        val texto = findViewById<TextView>(R.id.tvBienvenidaUsuario)
        texto.text = "Bienvenido, Usuario 👤"
    }
}
