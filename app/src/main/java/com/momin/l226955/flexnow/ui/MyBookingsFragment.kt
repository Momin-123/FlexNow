package com.momin.l226955.flexnow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.momin.l226955.flexnow.R
import com.momin.l226955.flexnow.databinding.FragmentMyBookingsBinding


class MyBookingsFragment : Fragment() {

    private var _binding: FragmentMyBookingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookingAdapter: MyBookingsAdapter
    private lateinit var bookedClassList: List<ClassSchedule>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create a test booked class (sample data)
        bookedClassList = listOf(
            ClassSchedule(R.drawable.yoga, "10:00 AM", "Instructor A", "Yoga", 5, true) // Marked as booked
        )

        // Initialize the adapter with the booked class list
        bookingAdapter = MyBookingsAdapter(bookedClassList,
            onCancelBookingClick = { classSchedule ->
                // Handle Cancel Booking button click
                Toast.makeText(requireContext(), "Booking Cancelled: ${classSchedule.className}", Toast.LENGTH_SHORT).show()
                // Implement your cancel booking logic here (e.g., remove class from list)
            },
            onJoinClassClick = { classSchedule ->
                // Handle Join Class button click
                Toast.makeText(requireContext(), "Joining Class: ${classSchedule.className}", Toast.LENGTH_SHORT).show()
                // Implement your join class logic here (e.g., open a Zoom link or another activity)
            }
        )

        // Set up the RecyclerView to show the booked classes
        binding.recyclerViewMyBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMyBookings.adapter = bookingAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
