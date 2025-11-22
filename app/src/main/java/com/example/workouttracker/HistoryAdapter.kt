package com.example.workouttracker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val workoutList: List<WorkoutItem>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutNameText: TextView = itemView.findViewById(R.id.workoutNameText)
        val workoutDateText: TextView = itemView.findViewById(R.id.workoutDateText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = workoutList[position]
        holder.workoutNameText.text = workout.name
        holder.workoutDateText.text = workout.date

        // Обработчик клика по элементу
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, WorkoutDetailActivity::class.java)
            intent.putExtra("WORKOUT_ITEM", workout)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = workoutList.size
}