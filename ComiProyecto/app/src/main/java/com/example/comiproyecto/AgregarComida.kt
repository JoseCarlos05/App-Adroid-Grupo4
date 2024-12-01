package com.example.comiproyecto

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ImageView
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
    private lateinit var dbH: SQLiteDatabase
    private lateinit var adapter: ComidaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_comida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = BDSQLite(this)
        dbH = db.writableDatabase
        Comida.insertarComidasHardcodeadas(dbH)

        val comidas = Comida.obtenerTodasLasComidas(dbH)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ComidaAdapter(comidas, dbH)
        recyclerView.adapter = adapter

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


        supportFragmentManager.beginTransaction()
            .replace(R.id.header, Header())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.footer, Footer())
            .commit()
    }
}