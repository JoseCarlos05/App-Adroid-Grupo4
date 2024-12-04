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

class ComidaAdapt(private val listaComidas: List<Map<String, Any>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewTypeDate = 0
    private val viewTypeComida = 1

    private val groupedComidas = listaComidas.groupBy { it["fecha"].toString() }
    private val items = mutableListOf<Any>()

    init {
        groupedComidas.forEach { (fecha, comidas) ->
            items.add(fecha)
            items.addAll(comidas)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) viewTypeDate else viewTypeComida
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == viewTypeDate) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fecha, parent, false)
            DateViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_comida, parent, false)
            ComidaViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DateViewHolder) {
            holder.bind(items[position] as String)
        } else if (holder is ComidaViewHolder) {
            holder.bind(items[position] as Map<String, Any>)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerFecha: TextView = itemView.findViewById(R.id.HeaderFecha)

        fun bind(fecha: String) {
            val today = Calendar.getInstance()
            val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

            val dateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
            val date = dateFormat.parse(fecha)

            headerFecha.text = when {
                dateFormat.format(today.time) == fecha -> "Hoy"
                dateFormat.format(yesterday.time) == fecha -> "Ayer"
                else -> fecha
            }
        }
    }

    class ComidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre: TextView = itemView.findViewById(R.id.NombreComida)
        private val cantidad: TextView = itemView.findViewById(R.id.CantidadComida)
        private val nutrientes: TextView = itemView.findViewById(R.id.NutrientesComida)
        private val imagenInfo: ImageView = itemView.findViewById(R.id.imagenInfo)

        @SuppressLint("SetTextI18n")
        fun bind(comida: Map<String, Any>) {
            nombre.text = comida["nombre"].toString()
            cantidad.text = comida["cantidad"].toString() + " g"
            val cantidad = comida["cantidad"] as? Int ?: 1

            val calorias = (comida["calorias"] as? Int ?: 0) * cantidad / 100
            val proteinas = (comida["proteinas"] as? Double ?: 0.0) * cantidad / 100
            val carbohidratos = (comida["carbohidratos"] as? Double ?: 0.0) * cantidad / 100
            val grasas = (comida["grasas"] as? Double ?: 0.0) * cantidad / 100
            val vitaminas = (comida["vitaminas"] as? Double ?: 0.0) * cantidad / 100
            val minerales = (comida["minerales"] as? Double ?: 0.0) * cantidad / 100

            nutrientes.text = "Calorías: $calorias kcal"

            imagenInfo.setOnClickListener {
                val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_comida_info, null)
                val dialogNombreComida: TextView = dialogView.findViewById(R.id.dialogNombreComida)
                val dialogInfoComida: TextView = dialogView.findViewById(R.id.dialogInfoComida)

                dialogNombreComida.text = comida["nombre"].toString()
                dialogInfoComida.text = "Calorías: $calorias kcal\nProteínas: ${String.format("%.2f", proteinas)} g\nCarbohidratos: ${String.format("%.2f", carbohidratos)} g\nGrasas: ${String.format("%.2f", grasas)} g\nVitaminas: ${String.format("%.2f", vitaminas)} g\nMinerales: ${String.format("%.2f", minerales)} g"

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