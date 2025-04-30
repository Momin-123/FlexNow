package com.momin.l226955.flexnow

import RecordedWorkout
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecordedWorkoutsFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val workoutList = listOf(
            RecordedWorkout(
                R.drawable.morningyoga,
                "John Doe",
                "April 20, 2025",
                "Morning Yoga",
                "android.resource://${requireContext().packageName}/${R.raw.samplevideo}"
            ),
            RecordedWorkout(
                R.drawable.hiit,
                "Jane Smith",
                "April 21, 2025",
                "HIIT Session",
                "android.resource://${requireContext().packageName}/${R.raw.samplevideo2}"
            ),
            RecordedWorkout(
                R.drawable.strenght,
                "Mike Tyson",
                "April 22, 2025",
                "Strength Training",
                "android.resource://${requireContext().packageName}/${R.raw.samplevideo3}"
            )
        )

        recyclerView.adapter = RecordedWorkoutAdapter(workoutList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recorded_workouts, container, false)
    }
}
