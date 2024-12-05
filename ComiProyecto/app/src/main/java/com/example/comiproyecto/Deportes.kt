package com.example.comiproyecto

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comiproyecto.BasedeDatos.BDSQLite
import com.example.comiproyecto.BasedeDatos.Modelos.Deporte
import com.example.comiproyecto.BasedeDatos.Modelos.Usuario
import com.example.comiproyecto.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class Deportes : AppCompatActivity() {

    private lateinit var cardAdapter: CardAdapter
    private var allCards: List<CardItem> = listOf()
    private var filteredCards: List<CardItem> = listOf()
    private var userDeportes: List<Int> = listOf() // Lista para almacenar los IDs de los deportes del usuario

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deportes)

        val db = BDSQLite(this)
        val dbH = db.writableDatabase
        val usuarioBD = Usuario(dbH)

        // Inserta deportes hardcodeados si no existen
        Deporte.insertarDeportesHardcodeados(dbH)

        // Recupera el ID del usuario desde SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("usuario", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("usuario_id", -1)

        val usuario = usuarioBD.buscarUsuarioPorID(usuarioId)

        // Inserta deportes de usuario (si no existen)
        Deporte.insertarDeportesUsuario(dbH, usuarioId)

        // Obtén todos los deportes desde la base de datos
        allCards = getCardsFromDatabase(dbH)
        filteredCards = allCards // Inicialmente muestra todas las cartas

        // Configuración del RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Establece el adaptador inicialmente con una lista vacía
        cardAdapter = CardAdapter(filteredCards, userDeportes)
        recyclerView.adapter = cardAdapter

        // Configuración del botón de filtro
        val filterButton: Button = findViewById(R.id.filter_button)

        filterButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetView: View = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)
            bottomSheetDialog.setContentView(bottomSheetView)

            val filterOption1: Button = bottomSheetView.findViewById(R.id.filter_option_1)
            val filterOption2: Button = bottomSheetView.findViewById(R.id.filter_option_2)
            val filterOption3: Button = bottomSheetView.findViewById(R.id.filter_option_3)

            filterOption1.setOnClickListener {
                applyFilter("Tonificar")
                bottomSheetDialog.dismiss()
            }

            filterOption2.setOnClickListener {
                applyFilter("Bajar de peso")
                bottomSheetDialog.dismiss()
            }

            filterOption3.setOnClickListener {
                applyFilter("Ganar masa muscular")
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.show()
        }

        val objetivo = usuario?.get("objetivo")?.toString()
        if (objetivo != null) {
            applyFilter(objetivo)
        } else {
            Toast.makeText(this, "Objetivo no reconocido", Toast.LENGTH_SHORT).show()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.header, Header())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.footer, Footer())
            .commit()

        // Cargar los deportes del usuario de manera asíncrona
        cargarDeportesDelUsuario(usuarioId)
    }

    // Método para obtener las cartas desde la base de datos
    private fun getCardsFromDatabase(db: SQLiteDatabase): List<CardItem> {
        val cardItems = mutableListOf<CardItem>()
        val cursor = db.query("deporte", null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            val objetivo = cursor.getString(cursor.getColumnIndexOrThrow("objetivo"))

            val iconResId = when (objetivo) {
                "Bajar de peso" -> R.drawable.bajarpeso
                "Tonificar" -> R.drawable.tonificar
                "Ganar masa muscular" -> R.drawable.masamuscular
                else -> R.drawable.icons8running50
            }

            cardItems.add(CardItem(id, nombre, descripcion, iconResId))
        }
        cursor.close()
        return cardItems
    }

    // Método para obtener los deportes del usuario de la base de datos
    private fun getUserDeportesFromDatabase(db: SQLiteDatabase, usuarioId: Int): List<Int> {
        val deporteIds = mutableListOf<Int>()
        val cursor = db.query(
            "usuario_deporte",
            arrayOf("id_deporte"),
            "id_usuario = ?",
            arrayOf(usuarioId.toString()),
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val idDeporte = cursor.getInt(cursor.getColumnIndexOrThrow("id_deporte"))
            deporteIds.add(idDeporte)
        }
        cursor.close()
        return deporteIds
    }

    // Método para cargar los deportes del usuario en segundo plano
    private fun cargarDeportesDelUsuario(usuarioId: Int) {
        val db = BDSQLite(this)

        // Ejecutar la consulta en segundo plano
        Thread {
            // Obtener los deportes del usuario desde la base de datos
            userDeportes = getUserDeportesFromDatabase(db.writableDatabase, usuarioId)

            // Actualizar el adaptador en el hilo principal
            runOnUiThread {
                // Actualizar el adaptador con los deportes del usuario
                cardAdapter = CardAdapter(filteredCards, userDeportes)
                val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
                recyclerView.adapter = cardAdapter
            }
        }.start()
    }

    // Aplicar filtro
    private fun applyFilter(objetivo: String) {
        filteredCards = allCards.filter { it.iconResId == when (objetivo) {
            "Bajar de peso" -> R.drawable.bajarpeso
            "Tonificar" -> R.drawable.tonificar
            "Ganar masa muscular" -> R.drawable.masamuscular
            else -> R.drawable.icons8running50
        }}
        cardAdapter.updateData(filteredCards)
    }
}



// Modelo de datos
class CardAdapter (
    private var cardList: List<CardItem>,
    private var userDeportes: List<Int> // IDs de deportes del usuario
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardList[position]
        holder.titleTextView.text = cardItem.title
        holder.descriptionTextView.text = cardItem.description
        holder.iconImageView.setImageResource(cardItem.iconResId)

        if (userDeportes.contains(cardItem.id)) {
            holder.objectiveTextView.visibility = View.VISIBLE
        } else {
            holder.objectiveTextView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = cardList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCardList: List<CardItem>) {
        cardList = newCardList
        notifyDataSetChanged()
    }

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.card_title)
        val descriptionTextView: TextView = view.findViewById(R.id.card_description)
        val iconImageView: ImageView = view.findViewById(R.id.card_icon)
        val objectiveTextView: TextView = view.findViewById(R.id.objective_text) // TextView de objetivo
    }
}

// Modelo actualizado para incluir el ID del deporte
data class CardItem(val id: Int, val title: String, val description: String, val iconResId: Int)
