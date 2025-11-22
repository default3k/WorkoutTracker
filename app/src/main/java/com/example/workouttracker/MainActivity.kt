package com.example.workouttracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Элементы круга
        val textCircleDayOfWeek = findViewById<TextView>(R.id.textCircleDayOfWeek)
        val textCircleDayMonth = findViewById<TextView>(R.id.textCircleDayMonth)
        val textCircleYear = findViewById<TextView>(R.id.textCircleYear)

        val btnAdd = findViewById<Button>(R.id.btn_add)
        val btnHistory = findViewById<Button>(R.id.btn_history)

        // ====== Установка даты ======
        val calendar = Calendar.getInstance()

        val formatDayOfWeek = SimpleDateFormat("EEEE", Locale("ru"))
        val formatDayMonth = SimpleDateFormat("d MMMM", Locale("ru"))
        val formatYear = SimpleDateFormat("yyyy", Locale("ru"))

        textCircleDayOfWeek.text = formatDayOfWeek.format(calendar.time)
        textCircleDayMonth.text = formatDayMonth.format(calendar.time)
        textCircleYear.text = formatYear.format(calendar.time)

        btnAdd.setOnClickListener {
            // Переход на страницу добавления тренировки
            val intent = Intent(this, AddWorkoutActivity::class.java)
            startActivity(intent)
        }

        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}