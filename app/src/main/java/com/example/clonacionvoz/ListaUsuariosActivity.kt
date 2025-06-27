package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var listaUsuarios: ListView
    private val db = FirebaseFirestore.getInstance()
    private val nombres = mutableListOf<String>()
    private val uids = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_usuarios)

        listaUsuarios = findViewById(R.id.listaUsuarios)

        db.collection("usuarios")
            .whereEqualTo("tipoUsuario", "usuario") // solo los usuarios regulares
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nombre = document.getString("nombre") ?: ""
                    val apellido = document.getString("apellido") ?: ""
                    val correo = document.getString("correo") ?: ""
                    val uid = document.getString("uid") ?: ""

                    nombres.add("$nombre $apellido\n📧 $correo")
                    uids.add(uid)
                }

                val adapter = object : ArrayAdapter<String>(this, R.layout.item_lista_usuario, R.id.tvNombreItem, nombres) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getView(position, convertView, parent)
                        val nombreView = view.findViewById<TextView>(R.id.tvNombreItem)
                        val correoView = view.findViewById<TextView>(R.id.tvCorreoItem)
                        val partes = nombres[position].split("\n📧 ")
                        if (partes.size == 2) {
                            nombreView.text = partes[0]
                            correoView.text = "📧 ${partes[1]}"
                        }
                        return view
                    }
                }

                listaUsuarios.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar usuarios", Toast.LENGTH_SHORT).show()
            }

        listaUsuarios.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, DetalleUsuarioActivity::class.java)
            intent.putExtra("uidUsuario", uids[position])
            startActivity(intent)
        }
    }
}

