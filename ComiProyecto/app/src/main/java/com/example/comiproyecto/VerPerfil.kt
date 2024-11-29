package com.example.comiproyecto

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comiproyecto.BasedeDatos.BDSQLite
import com.example.comiproyecto.BasedeDatos.Modelos.Usuario
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VerPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Conexión principal a la base de datos
        val db = BDSQLite(this)
        val dbH = db.writableDatabase
        val usuarioBD = Usuario(dbH)

        //Se recupera el id del usuario que inició sesión
        val sharedPreferences: SharedPreferences = getSharedPreferences("usuario", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("usuario_id", -1)

        //Variable para editar los datos, por defecto desactivada
        var editable = false

        //Declaración de botones y textos del xml
        val editar = findViewById<ImageView>(R.id.botonEditar)

        val textoNombre = findViewById<EditText>(R.id.nombrePerfil)
        val textoCorreo = findViewById<EditText>(R.id.correoPerfil)
        val textoContrasena = findViewById<EditText>(R.id.contrasenaPerfil)
        val textoTelefono = findViewById<EditText>(R.id.telefonoPerfil)
        val textoAltura = findViewById<EditText>(R.id.alturaPerfil)
        val textoPeso = findViewById<EditText>(R.id.pesoPerfil)
        val textoFecha = findViewById<EditText>(R.id.fechnacPerfil)
        val botonGuardar = findViewById<Button>(R.id.botonGuardar)

        //Configuración para el campo de fecha
        val calendario = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendario.set(Calendar.YEAR, year)
            calendario.set(Calendar.MONTH, month)
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            textoFecha.setText(formatoFecha.format(calendario.time))
        }

        textoFecha.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //Configuración para el spinner con los objetivos de usuario
        val spinnerObjetivo: Spinner = findViewById(R.id.spObjetivo)

        val objetivos = resources.getStringArray(R.array.objetivos_array)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            objetivos
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerObjetivo.adapter = adapter

        //EditText y botón de guardar no editables por defecto
        textoNombre.isEnabled = false
        textoCorreo.isEnabled = false
        textoContrasena.isEnabled = false
        textoTelefono.isEnabled = false
        textoAltura.isEnabled = false
        textoPeso.isEnabled = false
        textoFecha.isEnabled = false
        spinnerObjetivo.isEnabled = false

        botonGuardar.isEnabled = false

        //Acción del botón de edición, desactiva o activa los campos para su edición
        editar.setOnClickListener {
            editable = !editable

            if (editable) {
                textoNombre.isEnabled = true
                textoCorreo.isEnabled = true
                textoContrasena.isEnabled = true
                textoTelefono.isEnabled = true
                textoAltura.isEnabled = true
                textoPeso.isEnabled = true
                textoFecha.isEnabled = true
                spinnerObjetivo.isEnabled = true

                botonGuardar.isEnabled = true
            } else {
                textoNombre.isEnabled = false
                textoCorreo.isEnabled = false
                textoContrasena.isEnabled = false
                textoTelefono.isEnabled = false
                textoAltura.isEnabled = false
                textoPeso.isEnabled = false
                textoFecha.isEnabled = false
                spinnerObjetivo.isEnabled = false

                botonGuardar.isEnabled = false
            }
        }

        if (usuarioId != -1) {

            val usuario = usuarioBD.buscarUsuarioPorID(usuarioId)

            textoNombre.setText(usuario?.get("nombre").toString())
            textoCorreo.setText(usuario?.get("correo").toString())
            textoContrasena.setText(usuario?.get("contrasena").toString())
            textoTelefono.setText(usuario?.get("telefono").toString())
            textoAltura.setText(usuario?.get("altura").toString())
            textoPeso.setText(usuario?.get("peso").toString())
            textoFecha.setText(usuario?.get("fecha_nac").toString())

            val objetivo = usuario?.get("objetivo").toString()
            val index = objetivos.indexOf(objetivo)
            spinnerObjetivo.setSelection(index)

        } else {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_LONG).show()
        }

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
            val intent = Intent(this, VerPerfil::class.java)
            startActivity(intent)
        }
    }
}