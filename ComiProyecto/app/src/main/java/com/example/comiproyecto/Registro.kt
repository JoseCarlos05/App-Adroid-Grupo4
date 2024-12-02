package com.example.comiproyecto

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
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
import com.example.comiproyecto.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Registro : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = BDSQLite(this)
        val dbW = db.writableDatabase
        val usuarioBD = Usuario(dbW)

        val textoNombre = findViewById<EditText>(R.id.campoNombre)
        val textoCorreo = findViewById<EditText>(R.id.campoCorreo)
        val textoContrasena = findViewById<EditText>(R.id.campoContrasena)
        val textoTelefono = findViewById<EditText>(R.id.campoTelefono)
        val textoAltura = findViewById<EditText>(R.id.campoAltura)
        val textoPeso = findViewById<EditText>(R.id.campoPeso)
        val fecha = findViewById<EditText>(R.id.campoFechaNacimiento)
        val botonAtras = findViewById<ImageView>(R.id.botonAtras)
        val botonRegistrar = findViewById<Button>(R.id.botonRegistrar)

        val spinnerObjetivo: Spinner = findViewById(R.id.spObjetivo)

        val objetivos = resources.getStringArray(R.array.objetivos_array)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            objetivos
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerObjetivo.adapter = adapter

        val calendario = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendario.set(Calendar.YEAR, year)
            calendario.set(Calendar.MONTH, month)
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            fecha.setText(formatoFecha.format(calendario.time))
        }

        fecha.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        botonAtras.setOnClickListener {
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }

        botonRegistrar.setOnClickListener {

            if (textoNombre.text.toString().isEmpty() || textoCorreo.text.toString().isEmpty() || textoContrasena.text.toString().isEmpty()
                || textoTelefono.text.toString().isEmpty() || textoAltura.text.toString().isEmpty() || textoPeso.text.toString().isEmpty()
                || fecha.text.toString().isEmpty()) {
                Toast.makeText(this, "Hay campos vacíos", Toast.LENGTH_LONG).show()

            } else {

                usuarioBD.insertarUsuario(textoNombre.text.toString(), textoCorreo.text.toString(), textoContrasena.text.toString(),
                    textoTelefono.text.toString(), textoAltura.text.toString().toDouble(), textoPeso.text.toString().toDouble(),
                    fecha.text.toString(), spinnerObjetivo.selectedItem.toString())

                Toast.makeText(this, "Usuario ${textoNombre.text} registrado", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, InicioSesion::class.java)
                startActivity(intent)
            }
        }
    }
}
