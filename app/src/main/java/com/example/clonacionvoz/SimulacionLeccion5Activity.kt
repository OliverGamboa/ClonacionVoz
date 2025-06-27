package com.example.clonacionvoz

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class SimulacionLeccion5Activity : AppCompatActivity() {

    private lateinit var mensajeFinal: TextView
    private lateinit var btnGrabar1: Button
    private lateinit var btnGrabar2: Button
    private lateinit var btnOpcion1: Button
    private lateinit var btnOpcion2: Button
    private lateinit var btnOpcion3: Button
    private lateinit var feedback1: TextView
    private lateinit var feedback2: TextView
    private lateinit var feedback3: TextView
    private lateinit var layoutOpciones: LinearLayout
    private lateinit var btnIrEvaluacion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulacion_leccion5)

        mensajeFinal = findViewById(R.id.tvMensajeFinal)
        btnGrabar1 = findViewById(R.id.btnGrabar1)
        btnGrabar2 = findViewById(R.id.btnGrabar2)
        layoutOpciones = findViewById(R.id.layoutOpciones)
        btnIrEvaluacion = findViewById(R.id.btnIrEvaluacion5)

        feedback1 = findViewById(R.id.feedbackOpcion1)
        feedback2 = findViewById(R.id.feedbackOpcion2)
        feedback3 = findViewById(R.id.feedbackOpcion3)

        btnOpcion1 = findViewById(R.id.btnOpcion1)
        btnOpcion2 = findViewById(R.id.btnOpcion2)
        btnOpcion3 = findViewById(R.id.btnOpcion3)

        // Solicita permiso de notificaciones si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }

        // Primera grabación simulada
        btnGrabar1.setOnClickListener {
            btnGrabar1.isEnabled = false
            btnGrabar1.text = "🎤 Grabando..."
            Handler(Looper.getMainLooper()).postDelayed({
                btnGrabar1.visibility = View.GONE
                btnGrabar2.visibility = View.VISIBLE
            }, 5000)
        }

        // Segunda grabación simulada
        btnGrabar2.setOnClickListener {
            btnGrabar2.isEnabled = false
            btnGrabar2.text = "🎤 Grabando..."
            Handler(Looper.getMainLooper()).postDelayed({
                btnGrabar2.visibility = View.GONE
                mensajeFinal.text = "✅ ¡Felicidades! Tu voz ha sido guardada con éxito."

                // Mostrar ambas notificaciones
                Toast.makeText(this, "Voz guardada exitosamente", Toast.LENGTH_LONG).show()
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    mostrarNotificacion()
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    mensajeFinal.text = "🔓 Ahora podremos tener control sobre ella...\ny usarla para defraudar personas. ¡Buen día!"
                    layoutOpciones.visibility = View.VISIBLE
                }, 6000)
            }, 5000)
        }

        // Opciones
        btnOpcion1.setOnClickListener {
            colorearBoton(btnOpcion1, false)
            feedback1.text = "❌ Que parezca confiable no significa que lo sea. Siempre hay que investigar antes."
            feedback2.text = ""
            feedback3.text = ""
            deshabilitarOpciones()
        }

        btnOpcion2.setOnClickListener {
            colorearBoton(btnOpcion2, false)
            feedback1.text = ""
            feedback2.text = "❌ Conceder permisos sin entenderlos es una gran puerta para el fraude."
            feedback3.text = ""
            deshabilitarOpciones()
        }

        btnOpcion3.setOnClickListener {
            colorearBoton(btnOpcion3, true)
            feedback1.text = ""
            feedback2.text = ""
            feedback3.text = "✅ ¡Muy bien! Antes de usar cualquier aplicación, se debe verificar si es segura y quién la desarrolló."
            deshabilitarOpciones()
        }

        btnIrEvaluacion.setOnClickListener {
            startActivity(Intent(this, Evaluacion5Activity::class.java))
        }
    }

    // Permiso post-notificaciones (respuesta)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de notificación concedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso de notificación denegado", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deshabilitarOpciones() {
        btnOpcion1.isEnabled = false
        btnOpcion2.isEnabled = false
        btnOpcion3.isEnabled = false
    }

    private fun colorearBoton(boton: Button, esCorrecta: Boolean) {
        val color = if (esCorrecta) R.color.verde_correcto else R.color.rojo_incorrecto
        boton.backgroundTintList = ContextCompat.getColorStateList(this, color)
        boton.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    }

    // Notificación real del sistema
    private fun mostrarNotificacion() {
        val channelId = "voz_guardada"
        val notificationId = 101

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal de voz guardada"
            val descriptionText = "Notificación simulada de grabación"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_mic)
            .setContentTitle("Voz guardada exitosamente")
            .setContentText("Tu grabación ha sido almacenada en nuestros servidores.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }
}
