package com.momin.l226955.flexnow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.momin.l226955.flexnow.*
import com.momin.l226955.flexnow.databinding.FragmentTrainerBinding

class TrainerFragment : Fragment() {

    private var _binding: FragmentTrainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTrainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trainerList = listOf(
            Trainer("John Smith", "Certified Fitness Trainer with 10+ years of experience in strength training, cardio, and nutrition coaching. Passionate about helping clients reach their goals safely.", R.drawable.trainer1),
            Trainer("Emily Johnson", "Experienced Yoga Instructor and Wellness Coach, specialized in Vinyasa, Hatha, and breathing techniques. Focuses on body-mind balance and stress reduction.", R.drawable.trainer1),
            Trainer("Michael Lee", "Strength and Conditioning Specialist with a background in athletics. Works with beginners and professionals to build core strength and endurance effectively.", R.drawable.trainer1),
            Trainer("Sarah Kim", "Certified Personal Trainer focusing on functional fitness, bodyweight workouts, and mobility. Designs personalized routines to suit all fitness levels.", R.drawable.trainer1)
        )

        val trainerAdapter = TrainerAdapter(trainerList)
        binding.trainerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.trainerRecyclerView.adapter = trainerAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
