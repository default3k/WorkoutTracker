package com.example.workouttracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.Serializable

class WorkoutDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "workout.db"
        private const val DATABASE_VERSION = 2 // Увеличиваем версию
        private const val TABLE_WORKOUTS = "workouts"
        private const val TABLE_EXERCISES = "exercises"
        private const val KEY_ID = "id"
        private const val KEY_WORKOUT_ID = "workout_id"
        private const val KEY_NAME = "name"
        private const val KEY_DATE = "date"
        private const val KEY_NOTES = "notes"
        private const val KEY_EXERCISE_NAME = "exercise_name"
        private const val KEY_REPS = "reps"
        private const val KEY_WEIGHT = "weight"
        private const val KEY_SETS = "sets"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("WorkoutDatabase", "Creating database tables")

        // Таблица тренировок
        val createWorkoutsTable = """
            CREATE TABLE $TABLE_WORKOUTS(
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_NAME TEXT NOT NULL,
                $KEY_DATE TEXT NOT NULL,
                $KEY_NOTES TEXT
            )
        """.trimIndent()

        // Таблица упражнений
        val createExercisesTable = """
            CREATE TABLE $TABLE_EXERCISES(
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_WORKOUT_ID INTEGER NOT NULL,
                $KEY_EXERCISE_NAME TEXT NOT NULL,
                $KEY_REPS TEXT,
                $KEY_WEIGHT TEXT,
                $KEY_SETS TEXT,
                FOREIGN KEY($KEY_WORKOUT_ID) REFERENCES $TABLE_WORKOUTS($KEY_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        db.execSQL(createWorkoutsTable)
        db.execSQL(createExercisesTable)
        Log.d("WorkoutDatabase", "Tables created successfully")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("WorkoutDatabase", "Upgrading database from version $oldVersion to $newVersion")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCISES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKOUTS")
        onCreate(db)
    }

    // Сохраняем тренировку с упражнениями
    fun saveWorkoutWithExercises(workout: WorkoutItem, exercises: List<Exercise>): Long {
        val db = this.writableDatabase
        var workoutId = -1L

        db.beginTransaction()
        try {
            // Сохраняем тренировку
            val workoutValues = ContentValues().apply {
                put(KEY_NAME, workout.name)
                put(KEY_DATE, workout.date)
                put(KEY_NOTES, workout.notes)
            }

            workoutId = db.insert(TABLE_WORKOUTS, null, workoutValues)

            if (workoutId != -1L) {
                // Сохраняем упражнения
                exercises.forEach { exercise ->
                    val exerciseValues = ContentValues().apply {
                        put(KEY_WORKOUT_ID, workoutId)
                        put(KEY_EXERCISE_NAME, exercise.name)
                        put(KEY_REPS, exercise.reps)
                        put(KEY_WEIGHT, exercise.weight)
                        put(KEY_SETS, exercise.sets)
                    }
                    db.insert(TABLE_EXERCISES, null, exerciseValues)
                }
            }

            db.setTransactionSuccessful()
            Log.d("WorkoutDatabase", "Workout saved with ID: $workoutId, exercises: ${exercises.size}")
        } catch (e: Exception) {
            Log.e("WorkoutDatabase", "Error saving workout: ${e.message}")
        } finally {
            db.endTransaction()
        }

        return workoutId
    }

    // Получаем все тренировки
    fun getAllWorkouts(): List<WorkoutItem> {
        val workoutList = mutableListOf<WorkoutItem>()
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_WORKOUTS,
            null,
            null,
            null,
            null,
            null,
            "$KEY_DATE DESC"
        )

        try {
            with(cursor) {
                while (moveToNext()) {
                    val workout = WorkoutItem(
                        id = getInt(getColumnIndexOrThrow(KEY_ID)),
                        name = getString(getColumnIndexOrThrow(KEY_NAME)),
                        date = getString(getColumnIndexOrThrow(KEY_DATE)),
                        notes = getString(getColumnIndexOrThrow(KEY_NOTES))
                    )
                    workoutList.add(workout)
                }
            }
            Log.d("WorkoutDatabase", "Loaded ${workoutList.size} workouts")
        } catch (e: Exception) {
            Log.e("WorkoutDatabase", "Error loading workouts: ${e.message}")
        } finally {
            cursor.close()
        }

        return workoutList
    }

    // Получаем упражнения для конкретной тренировки
    fun getExercisesForWorkout(workoutId: Int): List<Exercise> {
        val exerciseList = mutableListOf<Exercise>()
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_EXERCISES,
            null,
            "$KEY_WORKOUT_ID = ?",
            arrayOf(workoutId.toString()),
            null,
            null,
            KEY_ID
        )

        try {
            with(cursor) {
                while (moveToNext()) {
                    val exercise = Exercise(
                        name = getString(getColumnIndexOrThrow(KEY_EXERCISE_NAME)),
                        reps = getString(getColumnIndexOrThrow(KEY_REPS)),
                        weight = getString(getColumnIndexOrThrow(KEY_WEIGHT)),
                        sets = getString(getColumnIndexOrThrow(KEY_SETS))
                    )
                    exerciseList.add(exercise)
                }
            }
            Log.d("WorkoutDatabase", "Loaded ${exerciseList.size} exercises for workout $workoutId")
        } catch (e: Exception) {
            Log.e("WorkoutDatabase", "Error loading exercises: ${e.message}")
        } finally {
            cursor.close()
        }

        return exerciseList
    }

    // Удаляем тренировку (упражнения удалятся каскадно)
    fun deleteWorkout(workoutId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_WORKOUTS, "$KEY_ID = ?", arrayOf(workoutId.toString()))
        val success = result > 0
        Log.d("WorkoutDatabase", "Workout $workoutId deleted: $success")
        return success
    }

    // Обновляем тренировку
    fun updateWorkout(workout: WorkoutItem): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, workout.name)
            put(KEY_DATE, workout.date)
            put(KEY_NOTES, workout.notes)
        }

        val result = db.update(TABLE_WORKOUTS, values, "$KEY_ID = ?", arrayOf(workout.id.toString()))
        val success = result > 0
        Log.d("WorkoutDatabase", "Workout ${workout.id} updated: $success")
        return success
    }
}

// Data class для тренировки
data class WorkoutItem(
    val id: Int = 0,
    val name: String,
    val date: String,
    val notes: String = ""
) : Serializable

// Data class для упражнения
data class Exercise(
    val name: String,
    val reps: String,
    val weight: String,
    val sets: String
) : Serializable