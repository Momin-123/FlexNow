package com.momin.l226955.flexnow.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momin.l226955.flexnow.R
import com.momin.l226955.flexnow.databinding.ItemClassBookingBinding

class MyBookingsAdapter(
    private val bookedClassList: List<Map<String, Any>>,
    private val onCancelBookingClick: (Map<String, Any>) -> Unit,
    private val onJoinClassClick: (Map<String, Any>) -> Unit
) : RecyclerView.Adapter<MyBookingsAdapter.MyBookingsViewHolder>() {

    inner class MyBookingsViewHolder(val binding: ItemClassBookingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookingsViewHolder {
        val binding = ItemClassBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBookingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyBookingsViewHolder, position: Int) {
        val classData = bookedClassList[position]
        with(holder.binding) {
            val className = classData["className"] as? String ?: "Unknown Class"
            val instructorName = classData["instructorName"] as? String ?: "Unknown Instructor"
            val time = classData["time"] as? String ?: "No time specified"
            val bookingsCount = (classData["bookingsCount"] as? Long)?.toInt() ?: 0

            imageViewClass.setImageResource(getImageResId(classData["imageName"] as? String ?: ""))
            textViewClassName.text = className
            textViewInstructor.text = "Instructor: $instructorName"
            textViewTime.text = "Time: $time"
            textViewBookings.text = "Bookings: $bookingsCount"

            buttonCancelBooking.setOnClickListener {
                onCancelBookingClick(classData)
            }

            buttonJoinClass.setOnClickListener {
                onJoinClassClick(classData)
            }
        }
    }

    override fun getItemCount(): Int = bookedClassList.size

    private fun getImageResId(imageName: String): Int {
        return when (imageName.lowercase()) {
            "yoga" -> R.drawable.yoga
            "cardio" -> R.drawable.cardio
            "pilates" -> R.drawable.pilates
            "strength" -> R.drawable.strenght
            "hit" -> R.drawable.hiit
            else -> R.drawable.flexnow
        }
    }
}
