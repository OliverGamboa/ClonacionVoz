package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.clonacionvoz.R

class MenuLeccionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_lecciones)

        findViewById<Button>(R.id.btnLeccion1).setOnClickListener {
            startActivity(Intent(this, Leccion1Activity::class.java))
        }

        findViewById<Button>(R.id.btnLeccion2).setOnClickListener {
            startActivity(Intent(this, Leccion2Activity::class.java))
        }

        findViewById<Button>(R.id.btnLeccion3).setOnClickListener {
            startActivity(Intent(this, Leccion3Activity::class.java))
        }

        findViewById<Button>(R.id.btnLeccion4).setOnClickListener {
            startActivity(Intent(this, Leccion4Activity::class.java))
        }
        findViewById<Button>(R.id.btnLeccion5).setOnClickListener {
            startActivity(Intent(this, Leccion5Activity::class.java))
        }
    }
}