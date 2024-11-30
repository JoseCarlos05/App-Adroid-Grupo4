package com.example.comiproyecto.BasedeDatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class BDSQLite(contexto: Context) : SQLiteOpenHelper(contexto, nombreBD, null, versionBD) {

    companion object {
        const val nombreBD = "trackfit.db"
        const val versionBD = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "correo TEXT NOT NULL, " +
                "contrasena TEXT NOT NULL, " +
                "telefono TEXT CHECK(LENGTH(telefono) = 9), " +
                "altura REAL, " +
                "peso REAL, " +
                "fecha_nac TIMESTAMP, " +
                "objetivo TEXT CHECK(objetivo IN ('Tonificar', 'Bajar de peso', 'Ganar masa muscular')))");

        db.execSQL("CREATE TABLE comida (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "calorias REAL, " +
                "proteinas REAL, " +
                "carbohidratos REAL, " +
                "vitaminas REAL, " +
                "grasas REAL, " +
                "minerales REAL)");

        db.execSQL("CREATE TABLE deporte (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "objetivo TEXT CHECK(objetivo IN ('Tonificar', 'Bajar de peso', 'Ganar masa muscular')))");

        db.execSQL("CREATE TABLE usuario_comida (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_usuario INTEGER, " +
                "id_comida INTEGER, " +
                "fecha TIMESTAMP, " +
                "cantidad INTEGER, " +
                "FOREIGN KEY (id_usuario) REFERENCES usuarios(id), " +
                "FOREIGN KEY (id_comida) REFERENCES comida(id))");

        db.execSQL("CREATE TABLE usuario_deporte (" +
                "id_usuario INTEGER, " +
                "id_deporte INTEGER, " +
                "FOREIGN KEY (id_usuario) REFERENCES usuarios(id), " +
                "FOREIGN KEY (id_deporte) REFERENCES deporte(id))");
    }

    override fun onUpgrade(db: SQLiteDatabase?, version1: Int, version: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS usuario_deporte")
            db.execSQL("DROP TABLE IF EXISTS usuario_comida");
            db.execSQL("DROP TABLE IF EXISTS deporte");
            db.execSQL("DROP TABLE IF EXISTS comida");
            db.execSQL("DROP TABLE IF EXISTS usuarios");
            onCreate(db);
        }
    }

    /**
     * Método para insertar un nuevo usuario en la tabla `usuarios`.
     * @param nombre Nombre del usuario.
     * @param correo Correo electrónico del usuario.
     * @param contrasena Contraseña del usuario.
     * @param telefono Teléfono del usuario (opcional, debe tener 9 dígitos).
     * @param altura Altura del usuario en metros (opcional).
     * @param peso Peso del usuario en kilogramos (opcional).
     * @param fechaNac Fecha de nacimiento del usuario en formato "YYYY-MM-DD" (opcional).
     * @param objetivo Objetivo del usuario (opcional, valores: 'Tonificar', 'Bajar de peso', 'Ganar masa muscular').
     * @return ID del usuario insertado o -1 si ocurre un error.
     */
    fun insertarUsuario(
        nombre: String,
        correo: String,
        contrasena: String,
        telefono: String?,
        altura: Double?,
        peso: Double?,
        fechaNac: String?,
        objetivo: String?
    ): Long {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("contrasena", contrasena)
            put("telefono", telefono) // Puede ser null
            put("altura", altura)     // Puede ser null
            put("peso", peso)         // Puede ser null
            put("fecha_nac", fechaNac) // Puede ser null
            put("objetivo", objetivo) // Puede ser null
        }

        return try {
            val resultado = db.insert("usuarios", null, valores)
            db.close()
            resultado
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }
}
