package com.example.comiproyecto

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView

class MainActivity3 : AppCompatActivity() {
    private lateinit var comidaAdapter: ComidaAdapter
    private val comidas = listOf(
        Comida("Hamburguesa"),
        Comida("Pizza"),
        Comida("Tacos"),
        Comida("Hot dog"),
        Comida("Sushi"),
        Comida("Ensalada"),
        Comida("Pasta"),
        Comida("Pollo"),
        Comida("Helado"),
        Comida("Pastel"),
        Comida("Galletas"),
        Comida("Donas")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        comidaAdapter = ComidaAdapter(comidas)
        recyclerView.adapter = comidaAdapter

        val buscador = findViewById<SearchView>(R.id.buscador)
        buscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredComidas = comidas.filter { it.nombre.contains(newText ?: "", ignoreCase = true) }
                comidaAdapter = ComidaAdapter(filteredComidas)
                recyclerView.adapter = comidaAdapter
                return true
            }
        })
    }
}