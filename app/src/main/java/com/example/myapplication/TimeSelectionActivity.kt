package com.example.myapplication

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class TimeSelectionActivity : AppCompatActivity() {
    private lateinit var timezone: String
    private lateinit var startTime: LocalTime
    private lateinit var endTime: LocalTime
    private lateinit var handler: Handler

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_selection)

        timezone = intent.getStringExtra("timezone") ?: "UTC"

        val startTimeButton = findViewById<Button>(R.id.startTimeButton)
        val endTimeButton = findViewById<Button>(R.id.endTimeButton)
        val imageView = findViewById<ImageView>(R.id.imageView)

        startTimeButton.setOnClickListener {
            pickTime { time -> startTime = time }
        }

        endTimeButton.setOnClickListener {
            pickTime { time -> endTime = time }
        }


        handler = Handler(Looper.getMainLooper())
        handler.post(updateRunnable(imageView))
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun pickTime(onTimePicked: (LocalTime) -> Unit) {
        val now = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                onTimePicked(LocalTime.of(hourOfDay, minute))
            },
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    private fun updateRunnable(imageView: ImageView): Runnable {
        return object : Runnable {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                val currentTime = ZonedDateTime.now(ZoneId.of(timezone)).toLocalTime()

                if (::startTime.isInitialized && ::endTime.isInitialized) {
                    if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
                        imageView.setImageResource(R.drawable.image1)
                    } else {
                        imageView.setImageResource(R.drawable.image2)
                    }
                }
                handler.postDelayed(this, 1000)
            }
        }
    }
}
