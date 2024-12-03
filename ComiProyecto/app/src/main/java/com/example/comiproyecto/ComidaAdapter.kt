package com.example.comiproyecto

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.comiproyecto.BasedeDatos.Modelos.Comida
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import android.widget.DatePicker
import android.widget.Toast

class ComidaAdapter(private var comidas: List<Comida>, private val db: SQLiteDatabase) : RecyclerView.Adapter<ComidaAdapter.ComidaViewHolder>(), Filterable {
    private var comidasFiltradas: List<Comida> = comidas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComidaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comida, parent, false)
        return ComidaViewHolder(view, db)
    }

    override fun onBindViewHolder(holder: ComidaViewHolder, position: Int) {
        val comida = comidasFiltradas[position]
        holder.bind(comida)
    }

    override fun getItemCount(): Int {
        return comidasFiltradas.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                comidasFiltradas = if (charString.isEmpty()) comidas else {
                    comidas.filter {
                        it.nombre.contains(charString, true)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = comidasFiltradas
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                comidasFiltradas = results?.values as List<Comida>
                notifyDataSetChanged()
            }
        }
    }

    class ComidaViewHolder(itemView: View, private val db: SQLiteDatabase) : RecyclerView.ViewHolder(itemView) {
        private val nombreComida: TextView = itemView.findViewById(R.id.nombreComida)
        private val botonAnadir: Button = itemView.findViewById(R.id.botonAnadir)
        private val imagenInfo: ImageView = itemView.findViewById(R.id.imagenInfo)

        @SuppressLint("MissingInflatedId")
        fun bind(comida: Comida) {
            nombreComida.text = comida.nombre

            botonAnadir.setOnClickListener {
                val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_comida_anadir, null)
                val dialogNombreComida: TextView = dialogView.findViewById(R.id.dialogNombreComida)
                val dialogCantidad: EditText = dialogView.findViewById(R.id.dialogCantidad)
                val dialogFecha: DatePicker = dialogView.findViewById(R.id.dialogFecha)

                dialogNombreComida.text = comida.nombre

                val dialog = AlertDialog.Builder(itemView.context)
                    .setView(dialogView)
                    .setPositiveButton("Aceptar", null)
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                dialog.setOnShowListener {
                    val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    button.setOnClickListener {
                        val cantidad = dialogCantidad.text.toString().toIntOrNull()
                        if (cantidad == null || cantidad <= 0) {
                            Toast.makeText(itemView.context, "La cantidad no puede estar vacía o ser 0", Toast.LENGTH_SHORT).show()
                        } else {
                            val day = dialogFecha.dayOfMonth
                            val month = dialogFecha.month
                            val year = dialogFecha.year
                            val fecha = "$year-${month + 1}-$day"

                            val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("usuario", MODE_PRIVATE)
                            val usuarioId = sharedPreferences.getInt("usuario_id", -1)

                            val values = ContentValues().apply {
                                put("id_comida", comida.id)
                                put("fecha", fecha)
                                put("cantidad", cantidad)
                                put("id_usuario", usuarioId)
                            }
                            db.insert("usuario_comida", null, values)
                            dialog.dismiss()
                        }
                    }
                }
                dialog.show()
            }

            imagenInfo.setOnClickListener {
                val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_comida_info, null)
                val dialogNombreComida: TextView = dialogView.findViewById(R.id.dialogNombreComida)
                val dialogInfoComida: TextView = dialogView.findViewById(R.id.dialogInfoComida)

                dialogNombreComida.text = comida.nombre
                dialogInfoComida.text = "Calorías: ${comida.calorias} kcal\nProteínas: ${comida.proteina} g\nCarbohidratos: ${comida.carbohidratos} g\nVitaminas: ${comida.vitaminas} g\nGrasas: ${comida.grasas} g\nMinerales: ${comida.minerales} g"

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