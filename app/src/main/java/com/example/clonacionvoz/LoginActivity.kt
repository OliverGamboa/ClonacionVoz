package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val correo = findViewById<EditText>(R.id.etCorreo)
        val contrasena = findViewById<EditText>(R.id.etContrasena)
        val btnIniciar = findViewById<Button>(R.id.btnIniciarSesion)
        val mensajeError = findViewById<TextView>(R.id.tvError)
        val tvIrRegistro = findViewById<TextView>(R.id.tvIrRegistro)

        tvIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
            finish()
        }

        btnIniciar.setOnClickListener {
            val email = correo.text.toString().trim()
            val pass = contrasena.text.toString().trim()

            if (email.isBlank() || pass.isBlank()) {
                mensajeError.text = "Por favor completa todos los campos."
                mensajeError.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener { resultado ->
                    val uid = resultado.user?.uid
                    if (uid != null) {
                        db.collection("usuarios").document(uid).get()
                            .addOnSuccessListener { documento ->
                                val estaBaneado = documento.getBoolean("baneado") ?: false
                                if (estaBaneado) {
                                    mensajeError.text = "Este usuario ha sido baneado. Contacte al administrador."
                                    mensajeError.visibility = TextView.VISIBLE
                                    return@addOnSuccessListener
                                }

                                val tipo = documento.getString("tipoUsuario")
                                when (tipo) {
                                    "administrador" -> {
                                        startActivity(Intent(this, AdminMainActivity::class.java))
                                        finish()
                                    }
                                    "usuario" -> {
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    }
                                    else -> {
                                        mensajeError.text = "Tipo de usuario desconocido."
                                        mensajeError.visibility = TextView.VISIBLE
                                    }
                                }
                            }
                            .addOnFailureListener {
                                mensajeError.text = "No se pudo verificar el tipo de usuario."
                                mensajeError.visibility = TextView.VISIBLE
                            }
                    }
                }
                .addOnFailureListener {
                    mensajeError.text = "Error: ${it.localizedMessage}"
                    mensajeError.visibility = TextView.VISIBLE
                }
        }
    }
}




