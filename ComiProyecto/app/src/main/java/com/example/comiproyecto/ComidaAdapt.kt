package com.example.comiproyecto

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
// Adaptador personalizado para el RecyclerView que maneja la lista de comidas
class ComidaAdapt(private val listaComidas: List<Map<String, Any>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // Tipos de vista: 0 para fecha, 1 para comida
    private val viewTypeDate = 0
    private val viewTypeComida = 1

    // Agrupa las comidas por fecha
    private val groupedComidas = listaComidas.groupBy { it["fecha"].toString() }
    // Lista que contiene los elementos a mostrar, intercalando fechas y comidas
    private val items = mutableListOf<Any>()

    init {
        // Parse the dates and sort them in descending order
        val dateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val sortedDates = groupedComidas.keys.map { dateFormat.parse(it) to it }
            .sortedByDescending { it.first }
            .map { it.second }

        // Recorre cada grupo de comidas y agrega la fecha seguida de las comidas
        sortedDates.forEach { fecha ->
            items.add(fecha)
            items.addAll(groupedComidas[fecha] ?: emptyList())
        }
    }
    // Determina el tipo de vista basado en el tipo de elemento en la lista
    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) viewTypeDate else viewTypeComida
    }
    // Crea la vista correspondiente dependiendo del tipo de vista (fecha o comida)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == viewTypeDate) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fecha, parent, false)
            DateViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_comida, parent, false)
            ComidaViewHolder(view)
        }
    }

    // Vincula los datos a la vista (ya sea fecha o comida)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DateViewHolder) {
            // Si el ViewHolder es de tipo fecha, lo vincula con la fecha correspondiente
            holder.bind(items[position] as String)
        } else if (holder is ComidaViewHolder) {
            // Si el ViewHolder es de tipo comida, lo vincula con la comida correspondiente
            holder.bind(items[position] as Map<String, Any>)
        }
    }

    // Devuelve el número total de elementos en la lista
    override fun getItemCount(): Int {
        return items.size
    }
    // ViewHolder para los encabezados de fecha
    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerFecha: TextView = itemView.findViewById(R.id.HeaderFecha)
        // Vincula la fecha a la vista
        fun bind(fecha: String) {
            val today = Calendar.getInstance()
            val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

            val dateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
            val date = dateFormat.parse(fecha)
            // Determina si la fecha corresponde a hoy, ayer o una fecha anterior
            headerFecha.text = when {
                dateFormat.format(today.time) == fecha -> "Hoy"
                dateFormat.format(yesterday.time) == fecha -> "Ayer"
                else -> fecha
            }
        }
    }

    // ViewHolder para los elementos de comida
    class ComidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre: TextView = itemView.findViewById(R.id.NombreComida)
        private val cantidad: TextView = itemView.findViewById(R.id.CantidadComida)
        private val nutrientes: TextView = itemView.findViewById(R.id.NutrientesComida)
        private val imagenInfo: ImageView = itemView.findViewById(R.id.imagenInfo)

        // Vincula los datos de comida a la vista
        @SuppressLint("SetTextI18n")
        fun bind(comida: Map<String, Any>) {
            nombre.text = comida["nombre"].toString()
            cantidad.text = comida["cantidad"].toString() + " g"
            val cantidad = comida["cantidad"] as? Int ?: 1
            // Calcula las cantidades totales de nutrientes según la cantidad de la comida
            val calorias = (comida["calorias"] as? Int ?: 0) * cantidad
            val proteinas = (comida["proteinas"] as? Double ?: 0.0) * cantidad
            val carbohidratos = (comida["carbohidratos"] as? Double ?: 0.0) * cantidad
            val grasas = (comida["grasas"] as? Double ?: 0.0) * cantidad
            val vitaminas = (comida["vitaminas"] as? Double ?: 0.0) * cantidad
            val minerales = (comida["minerales"] as? Double ?: 0.0) * cantidad
            // Muestra las calorías en el TextView
            nutrientes.text = "Calorías: $calorias kcal"
            // Configura el clic en la imagen para mostrar más detalles en un diálogo
            imagenInfo.setOnClickListener {
                // Infla la vista del diálogo
                val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_comida_info, null)
                val dialogNombreComida: TextView = dialogView.findViewById(R.id.dialogNombreComida)
                val dialogInfoComida: TextView = dialogView.findViewById(R.id.dialogInfoComida)

                dialogNombreComida.text = comida["nombre"].toString()
                dialogInfoComida.text = "Calorías: $calorias kcal\nProteínas: ${String.format("%.2f", proteinas)} g\nCarbohidratos: ${String.format("%.2f", carbohidratos)} g\nGrasas: ${String.format("%.2f", grasas)} g\nVitaminas: ${String.format("%.2f", vitaminas)} g\nMinerales: ${String.format("%.2f", minerales)} g"

                // Crea y muestra el diálogo
                val dialog = AlertDialog.Builder(itemView.context)
                    .setView(dialogView)
                    .setPositiveButton("Cerrar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                dialog.show()
            }
        }
    }
}