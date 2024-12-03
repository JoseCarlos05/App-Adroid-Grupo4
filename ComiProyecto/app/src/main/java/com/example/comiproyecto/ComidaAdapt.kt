package com.example.comiproyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ComidaAdapt(private val listaComidas: List<Map<String, Any>>):
    RecyclerView.Adapter<ComidaAdapt.ComidaViewHolder>() {
    class ComidaViewHolder(obVi: View) : RecyclerView.ViewHolder(obVi) {
        val fecha = obVi.findViewById<TextView>(R.id.FechaComida)
        val nombre = obVi.findViewById<TextView>(R.id.NombreComida)
        val cantidad = obVi.findViewById<TextView>(R.id.CantidadComida)
        val nutrientes = obVi.findViewById<TextView>(R.id.NutrientesComida)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComidaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_comida, parent, false)
        return ComidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComidaViewHolder, position: Int) {
        val comida = listaComidas[position]
        val fechaCom = comida["fecha"].toString()

        val formato = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val hoy = formato.format(Date())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val ayer = formato.format(cal.time)
        println(fechaCom)

        holder.fecha.text = when (fechaCom) {
            hoy -> "Hoy"
            ayer -> "Ayer"
            else -> fechaCom
        }

        holder.nombre.text = comida["nombre"].toString()
        holder.cantidad.text = comida["cantidad"].toString()
        val cantidad = comida["cantidad"] as? Int ?: 1

        val calorias = (comida["calorias"] as? Int ?: 0) * cantidad
        val proteinas = (comida["proteinas"] as? Double ?: 0.0) * cantidad
        val carbohidratos = (comida["carbohidratos"] as? Double ?: 0.0) * cantidad
        val grasas = (comida["grasas"] as? Double ?: 0.0) * cantidad
        val vitaminas = (comida["vitaminas"] as? Double ?: 0.0) * cantidad
        val minerales = (comida["minerales"] as? Double ?: 0.0) * cantidad
        val imagenInfo: ImageView = holder.itemView.findViewById(R.id.imagenInfo)

        holder.nutrientes.text = "Calorías: $calorias kcal"

        imagenInfo.setOnClickListener {
            val dialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_comida_info, null)
            val dialogNombreComida: TextView = dialogView.findViewById(R.id.dialogNombreComida)
            val dialogInfoComida: TextView = dialogView.findViewById(R.id.dialogInfoComida)

            dialogNombreComida.text = comida["nombre"].toString()
            dialogInfoComida.text = "Calorías: $calorias kcal\nProteínas: ${String.format("%.2f", proteinas)} g\nCarbohidratos: ${String.format("%.2f", carbohidratos)} g\nGrasas: ${String.format("%.2f", grasas)} g\nVitaminas: ${String.format("%.2f", vitaminas)} g\nMinerales: ${String.format("%.2f", minerales)} g"

            val dialog = AlertDialog.Builder(holder.itemView.context)
                .setView(dialogView)
                .setPositiveButton("Cerrar") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return listaComidas.size
    }
}