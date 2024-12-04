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
    private var allCards: List<CardItem> = listOf() // Inicialmente vacía
    private var filteredCards: List<CardItem> = listOf() // Lista que almacena las cartas filtradas

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deportes)

        // Conexión principal a la base de datos
        val db = BDSQLite(this)
        val dbH = db.writableDatabase
        val usuarioBD = Usuario(dbH)

        // Inserta deportes hardcodeados si no existen
        Deporte.insertarDeportesHardcodeados(dbH)

        // Se recupera el id del usuario que inició sesión
        val sharedPreferences: SharedPreferences = getSharedPreferences("usuario", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("usuario_id", -1)

        val usuario = usuarioBD.buscarUsuarioPorID(usuarioId)

        Deporte.insertarDeportesUsuario(dbH, usuarioId)

        // Obtén los datos desde la base de datos y asigna a las listas
        allCards = getCardsFromDatabase(dbH)
        filteredCards = allCards // Inicialmente muestra todas las cartas

        // Configuración del RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Establecer el adaptador inicial con todas las cartas
        cardAdapter = CardAdapter(filteredCards)
        recyclerView.adapter = cardAdapter

        // Encuentra el botón que abrirá el BottomSheet
        val filterButton: Button = findViewById(R.id.filter_button)

        filterButton.setOnClickListener {
            // Crea un BottomSheetDialog
            val bottomSheetDialog = BottomSheetDialog(this)

            // Infla el layout para el BottomSheet
            val bottomSheetView: View = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)
            bottomSheetDialog.setContentView(bottomSheetView)

            // Configura los botones del BottomSheet para aplicar los filtros
            val filterOption1: Button = bottomSheetView.findViewById(R.id.filter_option_1)
            val filterOption2: Button = bottomSheetView.findViewById(R.id.filter_option_2)
            val filterOption3: Button = bottomSheetView.findViewById(R.id.filter_option_3)

            // Configura los listeners de los botones
            filterOption1.setOnClickListener {
                applyFilter("Bajar de peso") // Aplica el filtro 1
                bottomSheetDialog.dismiss()
            }

            filterOption2.setOnClickListener {
                applyFilter("Tonificar") // Aplica el filtro 2
                bottomSheetDialog.dismiss()
            }

            filterOption3.setOnClickListener {
                applyFilter("Ganar masa muscular") // Aplica el filtro 3
                bottomSheetDialog.dismiss()
            }

            // Muestra el BottomSheet
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
    }

    // Obtiene los datos desde la base de datos
    private fun getCardsFromDatabase(db: SQLiteDatabase): List<CardItem> {
        val cardItems = mutableListOf<CardItem>()
        val cursor = db.query("deporte", null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            val objetivo = cursor.getString(cursor.getColumnIndexOrThrow("objetivo"))

            // Asocia el objetivo con un icono correspondiente
            val iconResId = when (objetivo) {
                "Bajar de peso" -> R.drawable.bajarpeso
                "Tonificar" -> R.drawable.tonificar
                "Ganar masa muscular" -> R.drawable.masamuscular
                else -> R.drawable.icons8running50 // Icono por defecto en caso de no coincidir
            }

            cardItems.add(CardItem(nombre, descripcion, iconResId))
        }
        cursor.close()
        return cardItems
    }

    // Aplica el filtro basado en el objetivo
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
data class CardItem(val title: String, val description: String, val iconResId: Int)

// Adaptador para el RecyclerView
class CardAdapter(private var cardList: List<CardItem>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardList[position]
        holder.titleTextView.text = cardItem.title
        holder.descriptionTextView.text = cardItem.description
        holder.iconImageView.setImageResource(cardItem.iconResId)
    }

    override fun getItemCount(): Int = cardList.size

    // Actualiza los datos del adaptador
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCardList: List<CardItem>) {
        cardList = newCardList
        notifyDataSetChanged()  // Notifica al adaptador que los datos han cambiado
    }

    // ViewHolder para las cartas
    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.card_title)
        val descriptionTextView: TextView = view.findViewById(R.id.card_description)
        val iconImageView: ImageView = view.findViewById(R.id.card_icon)
    }
}