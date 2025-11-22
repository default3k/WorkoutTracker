package com.example.workouttracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class WorkoutDetailActivity : AppCompatActivity() {

    private lateinit var workout: WorkoutItem
    private lateinit var exercisesContainer: LinearLayout
    private lateinit var db: WorkoutDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_detail)

        // Получаем данные тренировки из Intent
        workout = intent.getSerializableExtra("WORKOUT_ITEM") as WorkoutItem
        db = WorkoutDatabase(this)
        exercisesContainer = findViewById(R.id.exercisesContainer)

        setupViews()
        setupButtonListeners()
        loadWorkoutData()
    }

    private fun setupViews() {
        // Заголовок с названием тренировки
        findViewById<TextView>(R.id.workoutDetailTitle).text = workout.name
    }

    private fun setupButtonListeners() {
        // Кнопка назад
        findViewById<LinearLayout>(R.id.backButton).setOnClickListener {
            finish()
        }

        // Кнопка удаления
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            showDeleteConfirmation()
        }

        // Кнопка редактирования
        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            editWorkout()
        }
    }

    private fun loadWorkoutData() {
        // Загружаем основные данные
        findViewById<TextView>(R.id.workoutNameText).text = workout.name
        findViewById<TextView>(R.id.workoutDateText).text = workout.date

        val notesText = findViewById<TextView>(R.id.notesText)
        notesText.text = if (workout.notes.isNotEmpty()) workout.notes else "Заметок нет"

        // Загружаем упражнения
        loadExercises()
    }

    private fun loadExercises() {
        val exercises = db.getExercisesForWorkout(workout.id)

        if (exercises.isEmpty()) {
            val placeholderView = TextView(this).apply {
                text = "Упражнения не добавлены"
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.darker_gray))
                setPadding(50, 16, 50, 16)
                gravity = android.view.Gravity.CENTER
            }
            exercisesContainer.addView(placeholderView)
        } else {
            exercises.forEach { exercise ->
                addExerciseView(exercise)
            }
        }
    }

    private fun addExerciseView(exercise: Exercise) {
        val exerciseView = LayoutInflater.from(this)
            .inflate(R.layout.item_exercise_detail, exercisesContainer, false)

        exerciseView.findViewById<TextView>(R.id.exerciseNameText).text = exercise.name
        exerciseView.findViewById<TextView>(R.id.repsText).text = "${exercise.reps} повтор."
        exerciseView.findViewById<TextView>(R.id.weightText).text = "${exercise.weight} кг"
        exerciseView.findViewById<TextView>(R.id.setsText).text = "${exercise.sets} подход."

        exercisesContainer.addView(exerciseView)
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Удаление тренировки")
            .setMessage("Вы уверены, что хотите удалить тренировку \"${workout.name}\"?")
            .setPositiveButton("Удалить") { _, _ ->
                deleteWorkout()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun deleteWorkout() {
        val success = db.deleteWorkout(workout.id)
        if (success) {
            CustomToast.showSuccess(this, "Тренировка удалена")
            finish()
        } else {
            CustomToast.showError(this, "Ошибка удаления")
        }
    }

    private fun editWorkout() {
        val intent = Intent(this, EditWorkoutActivity::class.java)
        intent.putExtra("WORKOUT_ITEM", workout)
        startActivity(intent)
        finish()
    }
}