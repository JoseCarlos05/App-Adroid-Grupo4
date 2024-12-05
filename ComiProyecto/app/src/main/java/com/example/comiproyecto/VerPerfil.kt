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

        // Establece la fecha máxima para evitar que se seleccione una fecha de menos de 5 años
        calendario.add(Calendar.YEAR, -5)
        val fechaMaxima = calendario.timeInMillis
        calendario.add(Calendar.YEAR, 5)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendario.set(Calendar.YEAR, year)
            calendario.set(Calendar.MONTH, month)
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            textoFecha.setText(formatoFecha.format(calendario.time))
        }

        textoFecha.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                dateSetListener,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            )
            // Aplica la restricción de fecha máxima
            datePickerDialog.datePicker.maxDate = fechaMaxima
            datePickerDialog.show()
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

                editar.setImageResource(R.drawable.icons8editar40blancoynegro)
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

                editar.setImageResource(R.drawable.icons8editar40)
            }
        }

        //Cargar los datos del usuario encontrado según el id
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

        //Acción de actualizar los datos del usuario con el botón guardar
        botonGuardar.setOnClickListener {
            val nuevoNombre = textoNombre.text.toString()
            val nuevoCorreo = textoCorreo.text.toString()
            val nuevoTelefono = textoTelefono.text.toString()

            val usuario = usuarioBD.buscarUsuarioPorID(usuarioId)
            val nombreOriginal = usuario?.get("nombre").toString()
            val correoOriginal = usuario?.get("correo").toString()
            val telefonoOriginal = usuario?.get("telefono").toString()

            // Validar solo si los valores fueron modificados
            if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty() || textoContrasena.text.toString().isEmpty() ||
                nuevoTelefono.isEmpty() || textoAltura.text.toString().isEmpty() || textoPeso.text.toString().isEmpty() ||
                textoFecha.text.toString().isEmpty()) {

                Toast.makeText(this, "Hay campos vacíos", Toast.LENGTH_LONG).show()

            } else if (nuevoNombre != nombreOriginal && usuarioBD.comprobarUsuarioNombre(nuevoNombre)) {
                Toast.makeText(this, "Este nombre de usuario ya está en uso", Toast.LENGTH_LONG).show()

            } else if (nuevoCorreo != correoOriginal && usuarioBD.comprobarUsuarioCorreo(nuevoCorreo)) {
                Toast.makeText(this, "Ya hay un usuario con este correo electrónico", Toast.LENGTH_LONG).show()

            } else if (nuevoTelefono != telefonoOriginal && usuarioBD.comprobarUsuarioTelefono(nuevoTelefono)) {
                Toast.makeText(this, "Ya hay un usuario con este teléfono en uso", Toast.LENGTH_LONG).show()

            } else if (nuevoTelefono.length != 9) {
                Toast.makeText(this, "Número de teléfono inválido", Toast.LENGTH_LONG).show()

            } else {
                usuarioBD.actualizarUsuario(
                    usuarioId,
                    nuevoNombre,
                    nuevoCorreo,
                    textoContrasena.text.toString(),
                    nuevoTelefono,
                    textoAltura.text.toString().toDouble(),
                    textoPeso.text.toString().toDouble(),
                    textoFecha.text.toString(),
                    spinnerObjetivo.selectedItem.toString()
                )

                Toast.makeText(this, "Usuario ${nuevoNombre} actualizado", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, VerPerfil::class.java)
                startActivity(intent)
            }
        }


        //Configuración de footer y header
        supportFragmentManager.beginTransaction()
            .replace(R.id.header, Header())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.footer, Footer())
            .commit()
    }
}