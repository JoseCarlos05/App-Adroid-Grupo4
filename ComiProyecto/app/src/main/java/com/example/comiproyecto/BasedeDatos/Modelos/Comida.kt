package com.example.comiproyecto.BasedeDatos.Modelos

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

// Clase que representa una comida
data class Comida(val id: Int, val nombre: String, val calorias: Int, val proteina: Float, val carbohidratos: Float, val vitaminas: Float, val grasas: Float, val minerales: Float) {
    // Función para insertar una comida en la base de datos
    fun insertarComida(db: SQLiteDatabase) {
        val valores = ContentValues().apply {
            put("id", id)
            put("nombre", nombre)
            put("calorias", calorias)
            put("proteinas", proteina)
            put("carbohidratos", carbohidratos)
            put("vitaminas", vitaminas)
            put("grasas", grasas)
            put("minerales", minerales)
        }
        db.insert("comida", null, valores)
    }

    // Función para obtener todas las comidas de la base de datos
    companion object {
        // Función para insertar comidas hardcodeadas en la base de datos
        fun insertarComidasHardcodeadas(db: SQLiteDatabase) {
            val cursor = db.query("comida", null, null, null, null, null, null)
            if (cursor.count == 0) {
                val comidas = listOf(
                    Comida(1,"Pollo", 176, 32.7f, 0f, 0f, 5f, 1.1f),
                    Comida(2,"Huevo", 147, 12.58f, 0.77f, 0f, 9.94f, 1.1f),
                    Comida(3,"Pan", 266, 7.64f, 50.61f, 0f, 3.29f, 1.8f),
                    Comida(4,"Leche", 60, 3.22f, 4.52f, 0f, 3.25f, 0.28f),
                    Comida(5,"Lechuga", 14, 0.9f, 2.97f, 0f, 0.14f, 0.2f),
                    Comida(6,"Macarrones", 157, 5.76f, 30.68f, 0f, 0.92f, 0.7f),
                    Comida(7,"Manzana", 52, 0.26f, 13.81f, 0f, 0.17f, 2.4f),
                    Comida(8,"Plátano", 89, 1.09f, 22.84f, 0f, 0.33f, 2.6f),
                    Comida(9,"Queso", 402, 25f, 1.3f, 0f, 33f, 0f),
                    Comida(10,"Arroz", 130, 2.7f, 28.2f, 0f, 0.3f, 0.4f),
                    Comida(11,"Patata", 77, 2f, 17.58f, 0f, 0.1f, 2.2f),
                    Comida(12,"Yogur", 59, 3.5f, 4.66f, 0f, 1.5f, 0f),
                    Comida(13,"Pescado", 206, 22f, 0f, 0f, 12f, 0f),
                )

                for (comida in comidas) {
                    comida.insertarComida(db)
                }
            }
            cursor.close()
        }

        // Función para obtener todas las comidas de la base de datos
        fun obtenerTodasLasComidas(db: SQLiteDatabase): List<Comida> {
            val comidas = mutableListOf<Comida>()
            val cursor = db.query("comida", null, null, null, null, null, null)
            with(cursor) {
                while (moveToNext()) {
                    val id = getInt(getColumnIndexOrThrow("id"))
                    val nombre = getString(getColumnIndexOrThrow("nombre"))
                    val calorias = getInt(getColumnIndexOrThrow("calorias"))
                    val proteina = getFloat(getColumnIndexOrThrow("proteinas"))
                    val carbohidratos = getFloat(getColumnIndexOrThrow("carbohidratos"))
                    val vitaminas = getFloat(getColumnIndexOrThrow("vitaminas"))
                    val grasas = getFloat(getColumnIndexOrThrow("grasas"))
                    val minerales = getFloat(getColumnIndexOrThrow("minerales"))
                    comidas.add(Comida(id, nombre, calorias, proteina, carbohidratos, vitaminas, grasas, minerales))
                }
            }
            cursor.close()
            return comidas
        }
    }
}