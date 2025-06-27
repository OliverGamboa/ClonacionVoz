package com.example.clonacionvoz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EvaluacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluacion)

        findViewById<Button>(R.id.btnEvaluacion1).setOnClickListener {
            startActivity(Intent(this, Evaluacion1Activity::class.java))
        }

        findViewById<Button>(R.id.btnEvaluacion2).setOnClickListener {
            startActivity(Intent(this, Evaluacion2Activity::class.java))
        }

        findViewById<Button>(R.id.btnEvaluacion3).setOnClickListener {
            startActivity(Intent(this, Evaluacion3Activity::class.java))
        }

        findViewById<Button>(R.id.btnEvaluacion4).setOnClickListener {
            startActivity(Intent(this, Evaluacion4Activity::class.java))
        }
        findViewById<Button>(R.id.btnEvaluacion5).setOnClickListener {
            startActivity(Intent(this, Evaluacion5Activity::class.java))
        }
    }
}
