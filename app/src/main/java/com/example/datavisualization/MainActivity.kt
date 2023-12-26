package com.example.datavisualization

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val combinedEntries = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lineChart: LineChart = findViewById(R.id.lineChart)
        val systoleEntries = generateRandomEntries(12, 110f, 140f)
        val diastoleEntries = generateRandomEntries(12, 70f, 90f)
        setupChart(lineChart)
        combinedEntries.addAll(systoleEntries)
        combinedEntries.addAll(diastoleEntries)
        setData(lineChart, systoleEntries, diastoleEntries)
        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                // Menampilkan informasi detail pada titik yang disentuh
                if (e != null) {
                    val xAxisLabel = "Day ${e.x.toInt()}"
                    val yAxisLabel = "${e.y} mmHg"
                    val toastText = "$xAxisLabel ,$yAxisLabel"

                    Toast.makeText(this@MainActivity, toastText, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected() {
                Toast.makeText(this@MainActivity, "No data show", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupChart(chart: LineChart) {
        chart.setDrawGridBackground(false)
        chart.description = Description().apply { text = "Blood Pressure Pasien" }
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.setBackgroundColor(Color.WHITE)

        val legend: Legend = chart.legend
        legend.form = Legend.LegendForm.LINE

        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val dataIndex = value.toInt()
                return if (dataIndex >= 0 && dataIndex < combinedEntries.size) {
                    "Day ${dataIndex + 1}"
                } else {
                    ""
                }
            }
        }

        val yAxis: YAxis = chart.axisLeft
        yAxis.setDrawGridLines(false)
        yAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return "${value.toInt()}"
            }
        }
    }

    private fun setData(chart: LineChart, systoleEntries: List<Entry>, diastoleEntries: List<Entry>) {
        val systoleDataSet = LineDataSet(systoleEntries, "Systole")
        systoleDataSet.color = Color.RED
        systoleDataSet.setCircleColor(Color.GREEN)
        systoleDataSet.lineWidth = 2f
        systoleDataSet.circleRadius = 4f

        val diastoleDataSet = LineDataSet(diastoleEntries, "Diastole")
        diastoleDataSet.color = Color.BLUE
        diastoleDataSet.setCircleColor(Color.RED)
        diastoleDataSet.lineWidth = 2f
        diastoleDataSet.circleRadius = 4f

        val lineData = LineData(systoleDataSet, diastoleDataSet)
        chart.data = lineData
        chart.invalidate()
    }

    private fun generateRandomEntries(count: Int, minValue: Float, maxValue: Float): ArrayList<Entry> {
        val entries = ArrayList<Entry>()
        val random = Random()

        for (i in 0 until count) {
            val value = minValue + random.nextFloat() * (maxValue - minValue)
            entries.add(Entry((i + 1).toFloat(), value))
        }
        return entries
    }
}