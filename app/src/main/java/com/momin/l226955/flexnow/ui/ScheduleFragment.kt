package com.momin.l226955.flexnow.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.momin.l226955.flexnow.R
import com.momin.l226955.flexnow.databinding.FragmentScheduleBinding

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var scheduleAdapter: ClassScheduleAdapter
    private lateinit var scheduleList: MutableList<ClassSchedule> // Changed to MutableList for updates

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the schedule list with test data
        scheduleList = mutableListOf(
            ClassSchedule(R.drawable.yoga, "10:00 AM", "Instructor A", "Yoga", 5, false),
            ClassSchedule(R.drawable.cardio, "12:00 PM", "Instructor B", "Cardio Blast", 8, false),
            ClassSchedule(R.drawable.pilates, "3:00 PM", "Instructor C", "Pilates", 3, false)
        )

        // Initialize the adapter with the list of classes
        scheduleAdapter = ClassScheduleAdapter(scheduleList) { classSchedule ->
            // When Book Now is clicked, mark the class as booked
            if (!classSchedule.isBooked) {
                classSchedule.isBooked = true // Mark as booked
                // Optionally, update the booking count
                classSchedule.bookingsCount += 1
                scheduleAdapter.notifyDataSetChanged() // Refresh the list to show updated booking status

                // You can add a Toast to confirm the booking
                Toast.makeText(requireContext(), "Booked: ${classSchedule.className}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "${classSchedule.className} is already booked", Toast.LENGTH_SHORT).show()
            }
        }

        // Set the adapter and layout manager
        binding.recyclerViewSchedule.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSchedule.adapter = scheduleAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


