package com.example.comiproyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        holder.fecha.text = comida["fecha"].toString()
        holder.nombre.text = comida["nombre"].toString()
        holder.cantidad.text = comida["cantidad"].toString()

        val calorias = comida["calorias"] as? Int ?: 0
        val proteinas = comida["proteinas"] as? Double ?: 0.0
        val carbohidratos = comida["carbohidratos"] as? Double ?: 0.0
        val grasas = comida["grasas"] as? Double ?: 0.0
        holder.nutrientes.text = "Calorías: $calorias, Proteínas: $proteinas, Carbohidratos: $carbohidratos, Grasas: $grasas"
    }

    override fun getItemCount(): Int {
        return listaComidas.size
    }
}