package com.example.comiproyecto.BasedeDatos.Modelos

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.comiproyecto.BasedeDatos.BDSQLite

class Usuario(private val db: SQLiteDatabase) {

    fun insertarUsuario(nombre: String, correo: String, contrasena: String, telefono: String, altura: Double, peso: Double, fechaNac: String, objetivo: String): Long {
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("contrasena", contrasena)
            put("telefono", telefono)
            put("altura", altura)
            put("peso", peso)
            put("fecha_nac", fechaNac)
            put("objetivo", objetivo)
        }
        return db.insert("usuarios", null, valores)
    }

    fun buscarUsuarioPorID(id: Int): Map<String, Any>? {
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE id = ?",
            arrayOf(id.toString())
        )
        if (cursor.moveToFirst()) {
            val usuario = mapOf(
                "id" to cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                "nombre" to cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                "correo" to cursor.getString(cursor.getColumnIndexOrThrow("correo")),
                "contrasena" to cursor.getString(cursor.getColumnIndexOrThrow("contrasena")),
                "telefono" to cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                "altura" to cursor.getDouble(cursor.getColumnIndexOrThrow("altura")),
                "peso" to cursor.getDouble(cursor.getColumnIndexOrThrow("peso")),
                "fecha_nac" to cursor.getString(cursor.getColumnIndexOrThrow("fecha_nac")),
                "objetivo" to cursor.getString(cursor.getColumnIndexOrThrow("objetivo"))
            )
            cursor.close()
            return usuario
        } else {
            cursor.close()
            return null
        }
    }

    fun buscarUsuarioPorCorreoYContrasena(correo: String, contrasena: String): Map<String, Any>? {
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?",
            arrayOf(correo, contrasena)
        )
        if (cursor.moveToFirst()) {
            val usuario = mapOf(
                "id" to cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                "nombre" to cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                "correo" to cursor.getString(cursor.getColumnIndexOrThrow("correo")),
                "contrasena" to cursor.getString(cursor.getColumnIndexOrThrow("contrasena")),
                "telefono" to cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                "altura" to cursor.getDouble(cursor.getColumnIndexOrThrow("altura")),
                "peso" to cursor.getDouble(cursor.getColumnIndexOrThrow("peso")),
                "fecha_nac" to cursor.getString(cursor.getColumnIndexOrThrow("fecha_nac")),
                "objetivo" to cursor.getString(cursor.getColumnIndexOrThrow("objetivo"))
            )
            cursor.close()
            return usuario
        } else {
            cursor.close()
            return null
        }
    }
}