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
                "nombre TEXT, " +
                "correo TEXT NOT NULL, " +
                "contrasena TEXT NOT NULL," +
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

        db.execSQL("INSERT INTO usuarios (nombre, correo, contrasena, telefono, altura, peso, fecha_nac, objetivo) VALUES " +
                "('Juan Pérez', 'juan@mail.com', '1234', '987654321', 1.75, 70.5, '1990-05-20', 'Tonificar')," +
                "('Ana López', 'ana@mail.com', '5678', '912345678', 1.62, 58.0, '1992-08-15', 'Bajar de peso')");

        db.execSQL("INSERT INTO comida (nombre, calorias, proteinas, carbohidratos, vitaminas, grasas, minerales) VALUES " +
                "('Manzana', 52, 0.3, 14, 0.02, 0.2, 0.1)," +
                "('Pollo', 239, 27, 0, 0.05, 13, 1)");

        db.execSQL("INSERT INTO deporte (nombre, objetivo) VALUES " +
                "('Correr', 'Bajar de peso')," +
                "('Levantamiento de pesas', 'Ganar masa muscular')");
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