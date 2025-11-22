package com.example.workouttracker

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

object CustomToast {

    fun showSuccess(activity: AppCompatActivity, message: String) {
        showCustomToast(activity, message)
    }

    fun showError(activity: AppCompatActivity, message: String) {
        showCustomToast(activity, message)
    }

    fun showInfo(activity: AppCompatActivity, message: String) {
        showCustomToast(activity, message)
    }

    private fun showCustomToast(activity: AppCompatActivity, message: String) {
        try {
            val inflater = activity.layoutInflater
            val layout = inflater.inflate(R.layout.custom_toast, activity.findViewById(R.id.custom_toast_container))

            val textView = layout.findViewById<TextView>(R.id.toast_text)
            val imageView = layout.findViewById<ImageView>(R.id.toast_icon)

            textView.text = message
            imageView.setImageResource(R.drawable.logo) // Твое лого

            val toast = Toast(activity.applicationContext)
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
            toast.view = layout
            toast.show()
        } catch (e: Exception) {
            // Если кастомный Toast не работает, показываем обычный
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }
}