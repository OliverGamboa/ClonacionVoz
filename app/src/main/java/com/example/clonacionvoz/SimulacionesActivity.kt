package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SimulacionesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulaciones)

        // Botones para cada simulación
        findViewById<Button>(R.id.btnSimulacion1).setOnClickListener {
            startActivity(Intent(this, SimulacionLeccion1Activity::class.java))
        }

        findViewById<Button>(R.id.btnSimulacion2).setOnClickListener {
            startActivity(Intent(this, SimulacionLeccion2Activity::class.java))
        }

        findViewById<Button>(R.id.btnSimulacion3).setOnClickListener {
            startActivity(Intent(this, SimulacionLeccion3Activity::class.java))
        }

        findViewById<Button>(R.id.btnSimulacion4).setOnClickListener {
            startActivity(Intent(this, SimulacionLeccion4Activity::class.java))
        }
        findViewById<Button>(R.id.btnSimulacion5).setOnClickListener {
            startActivity(Intent(this, SimulacionLeccion5Activity::class.java))
        }
    }
}

