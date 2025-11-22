package com.example.workouttracker

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddWorkoutActivity : AppCompatActivity() {

    private lateinit var exercisesContainer: LinearLayout
    private val exercisesList = mutableListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)

        // Установка текущей даты по умолчанию
        val dateInput = findViewById<EditText>(R.id.workoutDateInput)
        val currentDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        dateInput.setText(currentDate)

        exercisesContainer = findViewById(R.id.exercisesContainer)

        setupButtonListeners()
        addExerciseField() // Добавляем первое упражнение по умолчанию
    }

    private fun setupButtonListeners() {
        // Стрелка назад
        findViewById<LinearLayout>(R.id.backButton).setOnClickListener {
            finish()
        }

        // Кнопка добавления упражнения
        findViewById<ImageButton>(R.id.btnAddExercise).setOnClickListener {
            addExerciseField()
        }

        // Кнопка удаления упражнения
        findViewById<ImageButton>(R.id.btnRemoveExercise).setOnClickListener {
            removeExerciseField()
        }

        // Кнопка сохранения
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveWorkout()
        }

        // Кнопка удаления (отмена)
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            finish()
        }
    }

    private fun addExerciseField() {
        val exerciseView = LayoutInflater.from(this)
            .inflate(R.layout.item_exercise, exercisesContainer, false)

        // Устанавливаем значения по умолчанию
        exerciseView.findViewById<EditText>(R.id.exerciseNameInput).setText("Упражнение ${exercisesContainer.childCount + 1}")
        exerciseView.findViewById<EditText>(R.id.repsInput).setText("12")
        exerciseView.findViewById<EditText>(R.id.weightInput).setText("0")
        exerciseView.findViewById<EditText>(R.id.setsInput).setText("3")

        exercisesContainer.addView(exerciseView)
    }

    private fun removeExerciseField() {
        if (exercisesContainer.childCount > 0) {
            exercisesContainer.removeViewAt(exercisesContainer.childCount - 1)
        } else {
            CustomToast.showError(this, "Нет упражнений для удаления")
        }
    }

    private fun saveWorkout() {
        val workoutName = findViewById<EditText>(R.id.workoutNameInput).text.toString().trim()
        val workoutDate = findViewById<EditText>(R.id.workoutDateInput).text.toString().trim()
        val notes = findViewById<EditText>(R.id.notesInput).text.toString().trim()

        // Проверка заполнения обязательных полей
        if (workoutName.isBlank()) {
            CustomToast.showError(this, "Введите название тренировки")
            return
        }

        if (workoutDate.isBlank()) {
            CustomToast.showError(this, "Введите дату тренировки")
            return
        }

        // Собираем упражнения
        val exercises = getExercisesFromForm()
        if (exercises.isEmpty()) {
            CustomToast.showError(this, "Добавьте хотя бы одно упражнение")
            return
        }

        // Создаем объект тренировки
        val workout = WorkoutItem(
            name = workoutName,
            date = workoutDate,
            notes = notes
        )

        // Сохраняем в базу данных
        val db = WorkoutDatabase(this)
        val result = db.saveWorkoutWithExercises(workout, exercises)

        if (result != -1L) {
            CustomToast.showSuccess(this, "Тренировка \"$workoutName\" сохранена!")
            finish()
        } else {
            CustomToast.showError(this, "Ошибка сохранения")
        }
    }

    private fun getExercisesFromForm(): List<Exercise> {
        val exercises = mutableListOf<Exercise>()

        for (i in 0 until exercisesContainer.childCount) {
            val exerciseView = exercisesContainer.getChildAt(i)
            val name = exerciseView.findViewById<EditText>(R.id.exerciseNameInput).text.toString().trim()
            val reps = exerciseView.findViewById<EditText>(R.id.repsInput).text.toString().trim()
            val weight = exerciseView.findViewById<EditText>(R.id.weightInput).text.toString().trim()
            val sets = exerciseView.findViewById<EditText>(R.id.setsInput).text.toString().trim()

            if (name.isNotBlank()) {
                exercises.add(Exercise(name, reps, weight, sets))
            }
        }

        return exercises
    }
}