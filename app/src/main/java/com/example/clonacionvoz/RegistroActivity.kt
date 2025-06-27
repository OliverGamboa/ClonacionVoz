package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nombre = findViewById<EditText>(R.id.etNombre)
        val apellido = findViewById<EditText>(R.id.etApellido)
        val correo = findViewById<EditText>(R.id.etCorreo)
        val contrasena = findViewById<EditText>(R.id.etContrasena)
        val codigo = findViewById<EditText>(R.id.etCodigoAdmin)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val mensajeError = findViewById<TextView>(R.id.tvError)

        val codigoAdmin = "clave1234"  // Puedes cambiar esta clave si lo deseas

        btnRegistrar.setOnClickListener {
            val nombreTexto = nombre.text.toString().trim()
            val apellidoTexto = apellido.text.toString().trim()
            val email = correo.text.toString().trim()
            val pass = contrasena.text.toString().trim()
            val codigoTexto = codigo.text.toString().trim()

            if (nombreTexto.isBlank() || apellidoTexto.isBlank() || email.isBlank() || pass.isBlank()) {
                mensajeError.text = "Por favor completa todos los campos obligatorios."
                mensajeError.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            val tipoUsuario = if (codigoTexto == codigoAdmin) "administrador" else "usuario"

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener { resultado ->
                    val uid = resultado.user?.uid
                    if (uid != null) {
                        val usuario = hashMapOf(
                            "uid" to uid,
                            "nombre" to nombreTexto,
                            "apellido" to apellidoTexto,
                            "correo" to email,
                            "tipoUsuario" to tipoUsuario,
                            "baneado" to false // Campo agregado automáticamente
                        )

                        db.collection("usuarios").document(uid).set(usuario)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registrado como $tipoUsuario", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                mensajeError.text = "Registro en base de datos falló."
                                mensajeError.visibility = TextView.VISIBLE
                            }
                    }
                }
                .addOnFailureListener {
                    mensajeError.text = "Error: ${it.localizedMessage}"
                    mensajeError.visibility = TextView.VISIBLE
                }
        }

        val tvIrLogin = findViewById<TextView>(R.id.tvIrLogin)
        tvIrLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}








