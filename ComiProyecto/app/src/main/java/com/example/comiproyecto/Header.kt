package com.example.comiproyecto

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment

class Header : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Instanciamos la vista del xml del header
        val view = inflater.inflate(R.layout.header, container, false)

        //Declaramos el botón de configuración como constante
        val botonConfig = view.findViewById<ImageView>(R.id.config)
        //Acción de que salga el menú al pulsar
        botonConfig.setOnClickListener { v ->
            //Variable que instancia el menú
            val popupMenu = PopupMenu(requireContext(), v)
            //Se infla con el respectivo xml
            popupMenu.menuInflater.inflate(R.menu.menu_config, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    //Acción cuando se seleccione el botón
                    R.id.logOut -> {
                        val intent = Intent(requireContext(), InicioSesion::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            //Se muestra el menú
            popupMenu.show()
        }

        return view
    }
}