package com.example.comiproyecto

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comiproyecto.BasedeDatos.BDSQLite
import com.example.comiproyecto.BasedeDatos.Modelos.Usuario
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class Estadistica : AppCompatActivity() {

    private lateinit var lineChart: LineChart

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estadistica)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración de footer y header
        supportFragmentManager.beginTransaction()
            .replace(R.id.header, Header())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.footer, Footer())
            .commit()

        // Inicializa el gráfico
        lineChart = findViewById(R.id.lineChart)
        setupChart()
        loadChartData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupChart() {
        // Configuración básica del gráfico
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.isEnabled = false // Eliminar la descripción predeterminada

        val db = BDSQLite(this)
        val dbH = db.writableDatabase
        val usuarioBD = Usuario(dbH)

        val sharedPreferences = getSharedPreferences("usuario", MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("usuario_id", -1)

        val usuario = usuarioBD.buscarUsuarioPorID(idUsuario)

        val edad = usuarioBD.calcularEdad(usuario?.get("fecha_nac").toString())

        //Recogida de datos y calculo de TMB
        val peso = (usuario?.get("peso") as Double )
        val altura = (usuario?.get("altura") as Double )

        var tmb = 10 * peso + 6.25 * altura - 5 * edad

        val objetivo = usuario?.get("objetivo").toString()

        if (objetivo == "Tonificar") {
            tmb -= 300
        } else if (objetivo == "Bajar de peso") {
            tmb -= 500
        } else if (objetivo == "Ganar masa muscular") {
            tmb += 400
        }

        //Configuración de la línea de la media de calorías
        Log.d("Objetivo", tmb.toString())
        val limitLine = LimitLine(tmb.toFloat(), "Objetivo de medía ($tmb kcal): $objetivo")
        limitLine.lineColor = R.color.black
        limitLine.lineWidth = 2f
        limitLine.textSize = 12f
        limitLine.textColor = R.color.black

        lineChart.axisLeft.addLimitLine(limitLine)

        lineChart.axisRight.addLimitLine(limitLine)

        lineChart.axisLeft.axisMaximum = 4000f
        lineChart.axisLeft.axisMinimum = 0f
        lineChart.axisRight.axisMaximum = 4000f
        lineChart.axisRight.axisMinimum = 0f
    }


    private fun loadChartData() {
        // Obtener las comidas y agruparlas por fecha
        val baseDatos = BDSQLite(this)
        val usuarioModel = Usuario(baseDatos.readableDatabase)

        val sharedPreferences = getSharedPreferences("usuario", MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("usuario_id", -1)

        // Obtener las comidas del usuario
        val comidas = usuarioModel.obtenerComidasDeUsuario(idUsuario)

        // Agrupar las comidas por fecha
        val groupedComidas = comidas.groupBy { it["fecha"].toString() }
            .entries
            .sortedBy { it.key }

        // Crear una lista de entradas para el gráfico
        val entries = ArrayList<Entry>()

        // Recorrer las fechas y calcular las calorías totales por día
        var dayIndex = 0f
        for ((fecha, comidasDelDia) in groupedComidas) {

            var caloriasTotales = 0

            for (comida in comidasDelDia) {
                val calorias = (comida["calorias"] as? Int ?: 0)
                val cantidad = (comida["cantidad"] as? Int ?: 0)

                val total = calorias * cantidad / 100

                caloriasTotales += total
            }

            entries.add(Entry(dayIndex++, caloriasTotales.toFloat()))
        }

        // Crear el dataset con los datos
        val dataSet = LineDataSet(entries, "Calorías diarias")
        dataSet.color = R.color.gris
        dataSet.setCircleColor(R.color.black)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 5f
        dataSet.valueTextSize = 15f

        // Crear un objeto LineData con el dataset
        val lineData = LineData(dataSet)

        // Asignar los datos al gráfico
        lineChart.data = lineData

        // Notificar al gráfico para que se actualice
        lineChart.invalidate()
    }

}
