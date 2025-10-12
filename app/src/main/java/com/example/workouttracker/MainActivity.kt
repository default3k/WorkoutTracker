package com.example.workouttracker

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd = findViewById<Button>(R.id.btn_add)
        val btnHistory = findViewById<Button>(R.id.btn_history)

        btnAdd.setOnClickListener {
            Toast.makeText(this, "Добавление тренировки", Toast.LENGTH_SHORT).show()
        }

        btnHistory.setOnClickListener {
            Toast.makeText(this, "Открытие истории", Toast.LENGTH_SHORT).show()
        }
    }
}
