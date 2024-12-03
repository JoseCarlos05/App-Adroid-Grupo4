package com.example.comiproyecto

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comiproyecto.BasedeDatos.BDSQLite
import com.example.comiproyecto.BasedeDatos.Modelos.Comida
import androidx.appcompat.widget.SearchView

class AgregarComida : AppCompatActivity() {
    // Declaración de variables
    private lateinit var dbH: SQLiteDatabase
    private lateinit var adapter: ComidaAdapter

    // Función que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_comida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Uso de la base de datos
        val db = BDSQLite(this)
        dbH = db.writableDatabase
        Comida.insertarComidasHardcodeadas(dbH)

        // Obtener todas las comidas
        val comidas = Comida.obtenerTodasLasComidas(dbH)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ComidaAdapter(comidas, dbH)
        recyclerView.adapter = adapter

        // Buscador de comidas
        val searchView = findViewById<SearchView>(R.id.buscador)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        // Header
        supportFragmentManager.beginTransaction()
            .replace(R.id.header, Header())
            .commit()

        // Footer
        supportFragmentManager.beginTransaction()
            .replace(R.id.footer, Footer())
            .commit()
    }
}