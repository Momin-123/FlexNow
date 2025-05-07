package com.momin.l226955.flexnow.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momin.l226955.flexnow.databinding.ItemClassScheduleBinding
class ClassScheduleAdapter(
    private val classSchedules: List<ClassSchedule>,
    private val onItemClick: (ClassSchedule) -> Unit
) : RecyclerView.Adapter<ClassScheduleAdapter.ClassScheduleViewHolder>() {

    inner class ClassScheduleViewHolder(val binding: ItemClassScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassScheduleViewHolder {
        val binding = ItemClassScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassScheduleViewHolder, position: Int) {
        val classSchedule = classSchedules[position]
        holder.binding.apply {
            imageViewClass.setImageResource(classSchedule.imageResId)
            textViewClassName.text = classSchedule.className
            textViewInstructor.text = "Instructor: ${classSchedule.instructorName}"
            textViewTime.text = "Time: ${classSchedule.time}"
            textViewBookings.text = "Bookings: ${classSchedule.bookingsCount}/${classSchedule.maxCapacity}"

            buttonBookNow.text = if (classSchedule.isBooked) "Booked" else "Book Now"
            buttonBookNow.isEnabled = !classSchedule.isBooked

            buttonBookNow.setOnClickListener {
                onItemClick(classSchedule)
            }
        }
    }

    override fun getItemCount() = classSchedules.size
}
