package com.example.comiproyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ComidaAdapter(private val comidas: List<Comida>) : RecyclerView.Adapter<ComidaAdapter.ComidaViewHolder>() {
    class ComidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombreComida)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComidaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comida, parent, false)
        return ComidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComidaViewHolder, position: Int) {
        val comida = comidas[position]
        holder.nombre.text = comida.nombre
    }

    override fun getItemCount(): Int = comidas.size
}