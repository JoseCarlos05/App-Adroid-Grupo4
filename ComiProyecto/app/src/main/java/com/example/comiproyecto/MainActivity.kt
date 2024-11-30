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

        val dbHelper = BDSQLite(this)
        val db = dbHelper.writableDatabase

        // Insertar datos en la tabla comida
        val nuevaComida = Comida("pepino", 47, 0.94f, 11.75f, 0f, 0.12f, 0.2f)
        nuevaComida.insertarComida(db)

        // Insertar datos en la tabla usuario_comida
        val idusu = 1 // ID del usuario
        val idComida = 7 // ID de la comida
        val fecha = "2023-10-01"
        val cantidad = 2
        nuevaComida.insertarUsuarioComida(idusu, idComida, fecha, cantidad, db)


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