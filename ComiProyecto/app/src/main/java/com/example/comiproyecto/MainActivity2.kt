package com.example.comiproyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comiproyecto.BasedeDatos.BDSQLite
import com.example.comiproyecto.BasedeDatos.Modelos.Usuario

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var editable = false

        val editar = findViewById<ImageView>(R.id.botonEditar)

        val textoNombre = findViewById<EditText>(R.id.nombrePerfil)
        val textoCorreo = findViewById<EditText>(R.id.correoPerfil)
        val textoTelefono = findViewById<EditText>(R.id.telefonoPerfil)

        val botonGuardar = findViewById<Button>(R.id.botonGuardar)

        textoNombre.isEnabled = false
        textoCorreo.isEnabled = false
        textoTelefono.isEnabled = false

        botonGuardar.isEnabled = false

        val db = BDSQLite(this)
        val dbH = db.writableDatabase
        val usuario = Usuario(dbH)

        val usuarioE = usuario.buscarUsuarioPorCorreoYContrasena("paco@gmail.com", "aifiesfgni")

        textoNombre.setText(usuarioE?.get("nombre").toString())
        textoCorreo.setText(usuarioE?.get("correo").toString())
        textoTelefono.setText(usuarioE?.get("telefono").toString())


        editar.setOnClickListener {
            editable = !editable

            if (editable) {

                textoNombre.isEnabled = true
                textoCorreo.isEnabled = true
                textoTelefono.isEnabled = true

                botonGuardar.isEnabled = true

            } else {

                textoNombre.isEnabled = false
                textoCorreo.isEnabled = false
                textoTelefono.isEnabled = false

                botonGuardar.isEnabled = false

            }

        }


        val botonPerfil = findViewById<ImageView>(R.id.botonPerfil)
        val botonInicio = findViewById<ImageView>(R.id.botonInicio)
        val botonAgregar = findViewById<ImageView>(R.id.botonAgregar)
        botonPerfil.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        botonInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        botonAgregar.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }
}