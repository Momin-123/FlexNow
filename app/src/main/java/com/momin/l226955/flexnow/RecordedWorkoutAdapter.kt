package com.momin.l226955.flexnow

import RecordedWorkout
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.momin.l226955.flexnow.R

class RecordedWorkoutAdapter(private val workoutList: List<RecordedWorkout>) :
    RecyclerView.Adapter<RecordedWorkoutAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        val workoutName: TextView = itemView.findViewById(R.id.workoutName)
        val instructorName: TextView = itemView.findViewById(R.id.instructorName)
        val uploadDate: TextView = itemView.findViewById(R.id.uploadDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recorded_workout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = workoutList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = workoutList[position]
        holder.thumbnail.setImageResource(workout.thumbnailResId)
        holder.workoutName.text = workout.workoutName
        holder.instructorName.text = "By: ${workout.instructorName}"
        holder.uploadDate.text = "Uploaded: ${workout.uploadDate}"

        holder.itemView.setOnClickListener {
            val workout = workoutList[position]
            val videoUriString = R.raw.samplevideo  // Raw resource ID

            if (videoUriString != 0) {  // Ensure the raw resource ID is valid
                val videoUri = Uri.parse("android.resource://" + it.context.packageName + "/" + videoUriString)

                val intent = Intent(it.context, VideoPlayerActivity::class.java)
                intent.putExtra("videoUri", videoUri.toString())  // Pass the URI as a string
                it.context.startActivity(intent)
            } else {
                Toast.makeText(it.context, "Video not available", Toast.LENGTH_SHORT).show()
            }
        }


    }
}