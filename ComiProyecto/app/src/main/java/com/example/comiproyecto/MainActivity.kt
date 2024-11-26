package com.example.comiproyecto

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val boton = findViewById<TextView>(R.id.texto)  //Creamos una variable con el botón a partir del XML

        boton.setOnClickListener {  //Al pulsar el botón...
            val intent = Intent(this, MainActivity2::class.java)  //Creamos un Intent para el MainActivity2.kt

            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            //Le añadimos al pendingIntent el Intent antes creado
            Handler(Looper.getMainLooper()).postDelayed({
                pendingIntent.send() }, 1000)  //Hacemos que pase de una pantalla a otra con retardo, le especificamos que tarde 10 segundos
        }
    }
}