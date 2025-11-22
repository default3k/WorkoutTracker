package com.example.workouttracker

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Обработчик для backButton
        findViewById<LinearLayout>(R.id.backButton).setOnClickListener {
            finish()
        }

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recycler = findViewById<RecyclerView>(R.id.historyRecycler)
        recycler.layoutManager = LinearLayoutManager(this)

        // Загружаем тренировки из базы данных
        val db = WorkoutDatabase(this)
        val workoutList = db.getAllWorkouts()

        recycler.adapter = HistoryAdapter(workoutList)

        // Показываем сообщение о количестве тренировок
        if (workoutList.isEmpty()) {
            CustomToast.showError(this, "Нет сохраненных тренировок")
        } else {
            CustomToast.showSuccess(this, "Загружено ${workoutList.size} тренировок")
        }
    }
}