package com.momin.l226955.flexnow.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momin.l226955.flexnow.databinding.ItemClassBookingBinding

class MyBookingsAdapter(
    private val bookedClassList: List<ClassSchedule>,
    private val onCancelBookingClick: (ClassSchedule) -> Unit,
    private val onJoinClassClick: (ClassSchedule) -> Unit
) : RecyclerView.Adapter<MyBookingsAdapter.MyBookingsViewHolder>() {

    inner class MyBookingsViewHolder(val binding: ItemClassBookingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookingsViewHolder {
        val binding = ItemClassBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBookingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyBookingsViewHolder, position: Int) {
        val classSchedule = bookedClassList[position]
        holder.binding.apply {
            imageViewClass.setImageResource(classSchedule.imageResId)
            textViewClassName.text = classSchedule.className
            textViewInstructor.text = "Instructor: ${classSchedule.instructorName}"
            textViewTime.text = "Time: ${classSchedule.time}"
            textViewBookings.text = "Bookings: ${classSchedule.bookingsCount}"

            // Set up Cancel and Join buttons
            buttonCancelBooking.setOnClickListener {
                onCancelBookingClick(classSchedule)
            }
            buttonJoinClass.setOnClickListener {
                onJoinClassClick(classSchedule)
            }
        }
    }

    override fun getItemCount() = bookedClassList.size
}
