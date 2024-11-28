package com.example.comiproyecto

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comiproyecto.BasedeDatos.BDSQLite
import com.example.comiproyecto.BasedeDatos.Modelos.Usuario

class InicioSesion : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_sesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences("usuario", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val db = BDSQLite(this)
        val dbW = db.writableDatabase
        val usuarioBD = Usuario(dbW)

        val correoTexto = findViewById<EditText>(R.id.correoLogin)
        val contrasenaTexto = findViewById<EditText>(R.id.contrasenaLogin)

        val botonLogin = findViewById<Button>(R.id.botonLogin)

        botonLogin.setOnClickListener {

            val usuario = usuarioBD.buscarUsuarioPorCorreoYContrasena(correoTexto.toString(), contrasenaTexto.toString())

            if (usuario != null) {
                editor.putInt("usuario", usuario["id"] as Int)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else if () {

            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show()
            }

        }
    }
}