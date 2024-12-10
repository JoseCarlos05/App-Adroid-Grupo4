package com.example.comiproyecto.BasedeDatos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BDSQLite(contexto: Context) : SQLiteOpenHelper(contexto, nombreBD, null, versionBD) {

    companion object {
        const val nombreBD = "trackfit.db"
        const val versionBD = 1
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT UNIQUE NOT NULL, " +
                "correo TEXT UNIQUE NOT NULL, " +
                "contrasena TEXT NOT NULL," +
                "telefono TEXT UNIQUE CHECK(LENGTH(telefono) = 9) NOT NULL, " +
                "altura REAL NOT NULL, " +
                "peso REAL NOT NULL, " +
                "fecha_nac TIMESTAMP NOT NULL, " +
                "objetivo TEXT CHECK(objetivo IN ('Tonificar', 'Bajar de peso', 'Ganar masa muscular')) NOT NULL )");
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
                "descripcion TEXT, " +
                "objetivo TEXT CHECK(objetivo IN ('Tonificar', 'Bajar de peso', 'Ganar masa muscular')))");
        db.execSQL("CREATE TABLE usuario_comida (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_usuario INTEGER, " +
                "id_comida INTEGER, " +
                "fecha TIMESTAMP, " +
                "cantidad REAL, " +
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
        };
    }
}