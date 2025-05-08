package com.momin.l226955.flexnow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.momin.l226955.flexnow.R
import com.momin.l226955.flexnow.Trainer
import com.momin.l226955.flexnow.TrainerAdapter
import com.momin.l226955.flexnow.databinding.FragmentTrainerBinding

class TrainerFragment : Fragment() {

    private var _binding: FragmentTrainerBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTrainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference.child("trainers")

        // Fetch trainer data from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val trainerList = mutableListOf<Trainer>()

                // Loop through all trainers in the database
                for (trainerSnapshot in snapshot.children) {
                    val name = trainerSnapshot.child("username").getValue(String::class.java) ?: ""
                    val bio = trainerSnapshot.child("description").getValue(String::class.java) ?: ""

                    // Set a placeholder image for now (using a drawable resource)
                    val profileImage = R.drawable.trainer1 // drawable resource ID for profile image

                    // Add the fetched trainer info to the list
                    val trainer = Trainer(name, bio, profileImage)
                    trainerList.add(trainer)
                }

                // Setup RecyclerView with the fetched data
                val trainerAdapter = TrainerAdapter(trainerList)
                binding.trainerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.trainerRecyclerView.adapter = trainerAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors here
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
