package com.momin.l226955.flexnow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainerAdapter(private val trainerList: List<Trainer>) : RecyclerView.Adapter<TrainerAdapter.TrainerViewHolder>() {

    class TrainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trainerImage: ImageView = itemView.findViewById(R.id.trainerImage)
        val trainerName: TextView = itemView.findViewById(R.id.trainerName)
        val trainerDescription: TextView = itemView.findViewById(R.id.trainerDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trainer, parent, false)
        return TrainerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainerViewHolder, position: Int) {
        val trainer = trainerList[position]
        holder.trainerImage.setImageResource(trainer.imageResId)
        holder.trainerName.text = trainer.name
        holder.trainerDescription.text = trainer.description
    }

    override fun getItemCount() = trainerList.size
}
