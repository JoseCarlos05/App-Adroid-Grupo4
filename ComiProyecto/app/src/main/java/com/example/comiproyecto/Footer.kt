package com.example.comiproyecto

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class Footer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.footer, container, false)

        val botonPerfil = view.findViewById<ImageView>(R.id.botonPerfil)
        val botonInicio = view.findViewById<ImageView>(R.id.botonInicio)
        val botonAgregar = view.findViewById<ImageView>(R.id.botonAgregar)

        botonPerfil.setOnClickListener {
            val intent = Intent(requireContext(), VerPerfil::class.java)
            startActivity(intent)
        }
        botonInicio.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        botonAgregar.setOnClickListener {
            val intent = Intent(requireContext(), AgregarComida::class.java)
            startActivity(intent)
        }

        return view
    }
}
