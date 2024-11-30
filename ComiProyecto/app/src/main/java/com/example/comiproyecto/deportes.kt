package com.example.comiproyecto

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.comiproyecto.BasedeDatos.Modelos.Usuario
import com.example.deportes.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class deportes : AppCompatActivity() {

    private lateinit var cardAdapter: CardAdapter
    private val allCards = getCardData()  // Datos completos de las cartas
    private var filteredCards = allCards  // Lista que almacena las cartas filtradas

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deportes)

        //Conexión principal a la base de datos
        val db = BDSQLite(this)
        val dbH = db.writableDatabase
        val usuarioBD = Usuario(dbH)

        //Se recupera el id del usuario que inició sesión
        val sharedPreferences: SharedPreferences = getSharedPreferences("usuario", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("usuario_id", -1)

        val usuario = usuarioBD.buscarUsuarioPorID(usuarioId)




        val botonPerfil = findViewById<ImageView>(R.id.botonPerfil)
        val botonInicio = findViewById<ImageView>(R.id.botonInicio)
        val botonAgregar = findViewById<ImageView>(R.id.botonAgregar)
        botonPerfil.setOnClickListener {
            val intent = Intent(this, VerPerfil::class.java)
            startActivity(intent)
        }
        botonInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        botonAgregar.setOnClickListener {
            val intent = Intent(this, deportes::class.java)
            startActivity(intent)
        }

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
                applyFilter(R.drawable.bajarpeso) // Aplica el filtro 1
                bottomSheetDialog.dismiss()
            }

            filterOption2.setOnClickListener {
                applyFilter(R.drawable.tonificar) // Aplica el filtro 2
                bottomSheetDialog.dismiss()
            }

            filterOption3.setOnClickListener {
                applyFilter(R.drawable.masamuscular) // Aplica el filtro 3
                bottomSheetDialog.dismiss()
            }

            // Muestra el BottomSheet
            bottomSheetDialog.show()
        }

        val objetivo = usuario?.get("objetivo")?.toString()
        when (objetivo) {
            "Tonificar" -> applyFilter(R.drawable.tonificar)
            "Bajar de peso" -> applyFilter(R.drawable.bajarpeso)
            "Ganar masa muscular" -> applyFilter(R.drawable.masamuscular)
            else -> {
                Toast.makeText(this, "Objetivo no reconocido", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun applyFilter(iconResId: Int) {
        filteredCards = allCards.filter { it.iconResId == iconResId }
        cardAdapter.updateData(filteredCards)  // Actualiza los datos del adaptador
    }

    // Datos de ejemplo para mostrar
    private fun getCardData(): List<CardItem> {
        return listOf(
            CardItem("Flexiones", "Descripción de la carta 1", R.drawable.bajarpeso),
            CardItem("Abdominales", "Descripción de la carta 2", R.drawable.tonificar),
            CardItem("Burpees", "Descripción de la carta 3", R.drawable.masamuscular),
            CardItem("Correr", "Descripción de la carta 1", R.drawable.bajarpeso),
            CardItem("Saltar", "Descripción de la carta 2", R.drawable.tonificar),
            CardItem("Saltar", "Descripción de la carta 3", R.drawable.masamuscular),
            CardItem("Dominadas", "Descripción de la carta 1", R.drawable.bajarpeso),
            CardItem("presbanca", "Descripción de la carta 2", R.drawable.tonificar),
            CardItem("comba", "Descripción de la carta 3", R.drawable.masamuscular)
        )
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
