package com.example.comiproyecto.BasedeDatos.Modelos

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class Deporte (val id: Int, val nombre: String, val descripcion: String, val objetivo: String) {

    fun insertarDeporte(db: SQLiteDatabase) {
        val valores = ContentValues().apply {
            put("id", id)
            put("nombre", nombre)
            put("descripcion", descripcion)
            put("objetivo", objetivo)
        }
        db.insert("deporte", null, valores)
    }

    companion object {
        fun insertarDeportesHardcodeados(db: SQLiteDatabase) {
            val cursor = db.query("deporte", null, null, null, null, null, null)
            if (cursor.count == 0) {
                val deportes = listOf(
                    Deporte(1, "Flexiones", "Ejercicio que trabaja pecho, hombros y tríceps. Realiza 5 repeticiones durante 5 minutos, asegurándote de mantener la espalda recta y bajar el pecho sin tocar el suelo.", "Tonificar"),
                    Deporte(2, "Correr", "Ejercicio cardiovascular que mejora la resistencia. Corre durante 20 minutos, mantén una postura erguida y respira de manera controlada.", "Tonificar"),
                    Deporte(3, "Dominadas", "Fortalece la espalda y los bíceps. Haz 12 repeticiones, descansando 11 minutos entre series. Usa asistencia si eres principiante.", "Tonificar"),
                    Deporte(4, "Abdominales", "Fortalece el core. Haz 8 repeticiones de crunches, descansando 5 minutos entre series. Mantén la espalda recta y respira controladamente.", "Bajar de peso"),
                    Deporte(5, "Saltar", "Mejora la explosividad y coordinación. Haz 20 repeticiones de saltos durante 8 minutos, manteniendo las rodillas ligeramente dobladas al aterrizar.", "Bajar de peso"),
                    Deporte(6, "Balón medicinal", "Ejercicio funcional que trabaja el core y los hombros. Realiza 8 repeticiones de lanzamientos o giros con el balón durante 5 minutos.", "Bajar de peso"),
                    Deporte(7, "Burpees", "Ejercicio de cuerpo completo que mejora la resistencia. Realiza 10 repeticiones durante 10 minutos, asegurándote de mantener una postura adecuada para evitar lesiones.", "Ganar masa muscular"),
                    Deporte(8, "Saltar", "Mejora la explosividad y coordinación. Haz 20 repeticiones de saltos durante 8 minutos, manteniendo las rodillas ligeramente dobladas al aterrizar.", "Ganar masa muscular"),
                    Deporte(9, "Comba", "Mejora la coordinación y cardiovascular. Haz 20 repeticiones de saltos durante 7 minutos, saltando con suavidad para proteger las articulaciones.", "Ganar masa muscular")
                )

                for (deporte in deportes) {
                    deporte.insertarDeporte(db)
                }
            }
            cursor.close()
        }

        // Inserta deportes en usuario_deporte basados en el objetivo del usuario
        fun insertarDeportesUsuario(db: SQLiteDatabase, usuarioId: Int) {
            // Paso 1: Elimina todos los datos de la tabla usuario_deporte
            db.delete("usuario_deporte", "id_usuario = ?", arrayOf(usuarioId.toString()))

            // Paso 2: Obtiene el objetivo del usuario
            val cursorUsuario = db.query(
                "usuarios",
                arrayOf("objetivo"), // Solo necesitamos el objetivo
                "id = ?",
                arrayOf(usuarioId.toString()),
                null,
                null,
                null
            )

            var objetivoUsuario: String? = null
            if (cursorUsuario.moveToFirst()) {
                objetivoUsuario = cursorUsuario.getString(cursorUsuario.getColumnIndexOrThrow("objetivo"))
            }
            cursorUsuario.close()

            if (objetivoUsuario != null) {
                // Paso 3: Consulta los deportes que tienen el mismo objetivo que el usuario
                val cursorDeportes = db.query(
                    "deporte",
                    arrayOf("id"), // Solo necesitamos el ID del deporte
                    "objetivo = ?",
                    arrayOf(objetivoUsuario),
                    null,
                    null,
                    null
                )

                // Paso 4: Inserta las relaciones en usuario_deporte
                while (cursorDeportes.moveToNext()) {
                    val deporteId = cursorDeportes.getInt(cursorDeportes.getColumnIndexOrThrow("id"))

                    // Verifica si la relación ya existe en usuario_deporte
                    val existeCursor = db.query(
                        "usuario_deporte",
                        null,
                        "id_usuario = ? AND id_deporte = ?",
                        arrayOf(usuarioId.toString(), deporteId.toString()),
                        null,
                        null,
                        null
                    )

                    if (existeCursor.count == 0) {
                        // Si no existe, inserta la relación
                        val valores = ContentValues().apply {
                            put("id_usuario", usuarioId)
                            put("id_deporte", deporteId)
                        }
                        db.insert("usuario_deporte", null, valores)
                    }
                    existeCursor.close()
                }
                cursorDeportes.close()
            }
        }

    }
}