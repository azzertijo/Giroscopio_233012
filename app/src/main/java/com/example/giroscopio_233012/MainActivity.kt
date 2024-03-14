package com.example.giroscopio_233012

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var gyroscopeSensor: Sensor? = null
    private lateinit var textViewGyroX: TextView
    private lateinit var textViewGyroY: TextView
    private lateinit var textViewGyroZ: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val defaultColor = Color.WHITE
        val savedColor = sharedPreferences.getInt("ultimoColor", defaultColor)

        window.decorView.setBackgroundColor(savedColor)

        val color = getColorName(savedColor)
        Toast.makeText(this, "Último color: $color", Toast.LENGTH_SHORT).show()

        textViewGyroX = findViewById(R.id.textView_gyro_x)
        textViewGyroY = findViewById(R.id.textView_gyro_y)
        textViewGyroZ = findViewById(R.id.textView_gyro_z)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    private fun getColorName(color: Int): String {
        return when (color) {
            Color.MAGENTA -> "Magenta"
            Color.BLUE -> "Azul"
            Color.CYAN -> "Cyan"
            Color.GREEN -> "Verde"
            Color.YELLOW -> "Amarillo"
            Color.RED -> "Rojo"
            else -> "Desconocido"
        }
    }

    override fun onResume() {
        super.onResume()
        gyroscopeSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_GYROSCOPE) {
                val gyroX = it.values[0]
                val gyroY = it.values[1]
                val gyroZ = it.values[2]

                textViewGyroX.text = "Eje X: $gyroX"
                textViewGyroY.text = "Eje Y: $gyroY"
                textViewGyroZ.text = "Eje Z: $gyroZ"


                var color = Color.WHITE

                if (gyroX != 0.0f) {
                    if (gyroX > 0.5f) {
                        color = Color.MAGENTA
                        Toast.makeText(this, "Magenta Eje X", Toast.LENGTH_SHORT).show()
                    } else if (gyroX < -0.5f) {
                        color = Color.BLUE
                        Toast.makeText(this, "Azul Eje X", Toast.LENGTH_SHORT).show()
                    }
                } else if (gyroY != 0.0f) {
                    if(gyroY > 0.5f){
                        color = Color.CYAN
                        Toast.makeText(this,"Cyan Eje Y", Toast.LENGTH_SHORT).show()
                    } else if(gyroY< -0.5f){
                        color = Color.GREEN
                        Toast.makeText(this,"Verde Eje Y", Toast.LENGTH_SHORT).show()
                    }
                } else if (gyroZ != 0.0f) {
                    if (gyroZ > 0.5f) {
                        color = Color.YELLOW
                        Toast.makeText(this, "Amarillo Eje Z", Toast.LENGTH_SHORT).show()
                    } else if (gyroZ < -0.5f) {
                        color = Color.RED
                        Toast.makeText(this, "Rojo Eje Z", Toast.LENGTH_SHORT).show()
                    }
                }

                if (color != Color.WHITE) {
                    val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("ultimoColor", color)
                    editor.apply()
                }

                window.decorView.setBackgroundColor(color)
            }
        }
    }
}
