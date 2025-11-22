package com.example.workouttracker

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EditWorkoutActivity : AppCompatActivity() {

    private lateinit var workout: WorkoutItem
    private lateinit var exercisesContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)

        // Получаем данные тренировки из Intent
        workout = intent.getSerializableExtra("WORKOUT_ITEM") as WorkoutItem
        exercisesContainer = findViewById(R.id.exercisesContainer)

        setupViews()
        setupButtonListeners()
        loadWorkoutData()
    }

    private fun setupViews() {
        // Меняем заголовок
        findViewById<TextView>(R.id.addWorkoutTitle).text = "Редактирование тренировки"

        // Меняем текст кнопки сохранения
        findViewById<Button>(R.id.btnSave).text = "Обновить"
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

        // Кнопка сохранения (обновления)
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            updateWorkout()
        }

        // Кнопка удаления тренировки
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            finish()
        }
    }

    private fun loadWorkoutData() {
        // Загружаем данные тренировки в форму
        findViewById<EditText>(R.id.workoutNameInput).setText(workout.name)
        findViewById<EditText>(R.id.workoutDateInput).setText(workout.date)
        findViewById<EditText>(R.id.notesInput).setText(workout.notes)

        // Пока просто добавляем одно пустое поле для упражнений
        // В будущем можно загрузить существующие упражнения из базы
        addExerciseField()
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

    private fun updateWorkout() {
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

        // Обновляем объект тренировки
        val updatedWorkout = workout.copy(
            name = workoutName,
            date = workoutDate,
            notes = notes
        )

        // Сохраняем в базу данных (пока как новую тренировку)
        val db = WorkoutDatabase(this)
        val result = db.saveWorkoutWithExercises(updatedWorkout, exercises)

        if (result != -1L) {
            // Удаляем старую тренировку
            db.deleteWorkout(workout.id)

            CustomToast.showSuccess(this, "Тренировка обновлена!")
            finish()
        } else {
            CustomToast.showError(this, "Ошибка обновления")
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