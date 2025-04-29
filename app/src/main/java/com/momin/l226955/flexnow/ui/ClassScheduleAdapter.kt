package com.momin.l226955.flexnow.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momin.l226955.flexnow.databinding.ItemClassScheduleBinding


class ClassScheduleAdapter(
    private val classList: List<ClassSchedule>,
    private val onBookNowClick: (ClassSchedule) -> Unit
) : RecyclerView.Adapter<ClassScheduleAdapter.ScheduleViewHolder>() {

    inner class ScheduleViewHolder(val binding: ItemClassScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemClassScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val classSchedule = classList[position]
        holder.binding.apply {
            imageViewClass.setImageResource(classSchedule.imageResId)
            textViewClassName.text = classSchedule.className
            textViewInstructor.text = "Instructor: ${classSchedule.instructorName}"
            textViewTime.text = "Time: ${classSchedule.time}"
            textViewBookings.text = "Bookings: ${classSchedule.bookingsCount}"

            // Set button text and enabled/disabled state
            if (classSchedule.isBooked) {
                buttonBookNow.text = "Booked"
                buttonBookNow.isEnabled = false // Disable if already booked
            } else {
                buttonBookNow.text = "Book Now"
                buttonBookNow.isEnabled = true // Enable if not booked
            }

            buttonBookNow.setOnClickListener {
                onBookNowClick(classSchedule)
            }
        }
    }

    override fun getItemCount() = classList.size
}


