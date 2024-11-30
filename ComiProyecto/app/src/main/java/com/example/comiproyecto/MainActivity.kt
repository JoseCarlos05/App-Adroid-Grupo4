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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comiproyecto.BasedeDatos.BDSQLite
import com.example.comiproyecto.BasedeDatos.Modelos.Comida
import com.example.comiproyecto.BasedeDatos.Modelos.Usuario

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




        val baseDatos = BDSQLite(this)
        val usuarioModel = Usuario(baseDatos.readableDatabase)
        val idUsuario = intent.getIntExtra("usuario_id", -1)
        val comidas = usuarioModel.obtenerComidasDeUsuario(idUsuario)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerComidas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adaptador = ComidaAdapt(comidas)
        recyclerView.adapter = adaptador
    }
}