package com.momin.l226955.flexnow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TrainerActivity : AppCompatActivity() {

    private lateinit var trainerRecyclerView: RecyclerView
    private lateinit var trainerAdapter: TrainerAdapter
    private lateinit var trainerList: List<Trainer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainer)

        trainerRecyclerView = findViewById(R.id.trainerRecyclerView)
        trainerRecyclerView.layoutManager = LinearLayoutManager(this)

        trainerList = listOf(
            Trainer("John Smith", "Certified Fitness Trainer with 10 years of experience.", R.drawable.trainer1),
            Trainer("Emily Johnson", "Yoga Instructor and Wellness Coach.", R.drawable.trainer1),
            Trainer("Michael Lee", "Strength and Conditioning Specialist.", R.drawable.trainer1),
            Trainer("Sarah Kim", "Personal Trainer focusing on functional fitness.", R.drawable.trainer1)
        )

        trainerAdapter = TrainerAdapter(trainerList)
        trainerRecyclerView.adapter = trainerAdapter
    }
}
