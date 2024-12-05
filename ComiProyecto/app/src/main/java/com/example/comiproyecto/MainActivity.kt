package com.example.comiproyecto

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
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

        supportFragmentManager.beginTransaction()
            .replace(R.id.header, Header())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.footer, Footer())
            .commit()

        // Crea una instancia de la clase BDSQLite para interactuar con la base de datos SQLite
        val baseDatos = BDSQLite(this)
        // modelo de usuario que interactúa con la base de datos
        val usuarioModel = Usuario(baseDatos.readableDatabase)

        // Accede a las preferencias compartidas para obtener el ID del usuario actual
        val sharedPreferences: SharedPreferences = getSharedPreferences("usuario", MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("usuario_id", -1)
        // Obtiene las comidas asociadas al usuario a partir de la base de datos
        val comidas = usuarioModel.obtenerComidasDeUsuario(idUsuario)

        // Configura el RecyclerView para mostrar las comidas
        val recyclerView: RecyclerView = findViewById(R.id.recyclerComidas)
        val emptyMessage: TextView = findViewById(R.id.empty_message)

        // Si no hay comidas, mostrar el mensaje vacío
        if (comidas.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyMessage.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyMessage.visibility = View.GONE
            recyclerView.layoutManager = LinearLayoutManager(this)
            // Crea el adaptador y lo asigna al RecyclerView
            val adaptador = ComidaAdapt(comidas)
            recyclerView.adapter = adaptador
        }
    }
}
