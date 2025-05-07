package com.momin.l226955.flexnow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.momin.l226955.flexnow.R
import com.momin.l226955.flexnow.databinding.FragmentScheduleBinding

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var scheduleAdapter: ClassScheduleAdapter
    private lateinit var scheduleList: MutableList<ClassSchedule>
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleList = mutableListOf()

        scheduleAdapter = ClassScheduleAdapter(scheduleList) { classSchedule ->
            val currentUserId = auth.currentUser?.uid
            if (currentUserId != null) {
                checkIfUserBookedClass(classSchedule, currentUserId)
            }
        }

        binding.recyclerViewSchedule.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSchedule.adapter = scheduleAdapter

        fetchClassSchedules()
    }

    private fun fetchClassSchedules() {
        val currentUserId = auth.currentUser?.uid ?: return

        db.collection("Classes").get()
            .addOnSuccessListener { classDocs ->
                scheduleList.clear()

                for (classDoc in classDocs) {
                    val className = classDoc.id
                    val classNameValue = classDoc.getString("className") ?: ""
                    val instructorName = classDoc.getString("instructorName") ?: ""
                    val time = classDoc.getString("time") ?: ""
                    val bookingsCount = classDoc.getLong("bookingsCount")?.toInt() ?: 0
                    val maxCapacity = classDoc.getLong("maxCapacity")?.toInt() ?: 20
                    val imageName = classDoc.getString("imageName") ?: ""

                    // For each class, check if user has booked
                    db.collection("class_bookings").document(className).get()
                        .addOnSuccessListener { bookingDoc ->
                            val bookedUsers = bookingDoc.get("bookedUsers") as? List<*> ?: emptyList<String>()
                            val isBooked = bookedUsers.contains(currentUserId)

                            val classSchedule = ClassSchedule(
                                imageResId = getImageResId(imageName),
                                time = time,
                                instructorName = instructorName,
                                className = className,
                                bookingsCount = bookingsCount,
                                maxCapacity = maxCapacity,
                                isBooked = isBooked
                            )

                            scheduleList.add(classSchedule)

                            // Refresh after all documents are loaded
                            if (scheduleList.size == classDocs.size()) {
                                scheduleAdapter.notifyDataSetChanged()
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch classes", Toast.LENGTH_SHORT).show()
            }
    }


    private fun checkIfUserBookedClass(classSchedule: ClassSchedule, userId: String) {
        db.collection("class_bookings")
            .document(classSchedule.className)
            .get()
            .addOnSuccessListener { document ->
                val bookedUsers = document.get("bookedUsers") as? List<String> ?: emptyList()
                if (bookedUsers.contains(userId)) {
                    Toast.makeText(requireContext(), "Already Booked", Toast.LENGTH_SHORT).show()
                } else {
                    bookClass(classSchedule)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error checking booking status", Toast.LENGTH_SHORT).show()
            }
    }

    private fun bookClass(classSchedule: ClassSchedule) {
        val currentUserId = auth.currentUser?.uid ?: return

        val classBookingsRef = db.collection("class_bookings").document(classSchedule.className)
        val classesRef = db.collection("Classes").document(classSchedule.className)

        // First, increment the booking count in the original class document
        classesRef.update("bookingsCount", FieldValue.increment(1))
            .addOnSuccessListener {
                // Update the class_bookings document with full class data + new user
                val updatedBookingData = hashMapOf(
                    "className" to classSchedule.className,
                    "instructorName" to classSchedule.instructorName,
                    "time" to classSchedule.time,
                    "imageName" to getImageNameFromResId(classSchedule.imageResId),
                    "bookingsCount" to classSchedule.bookingsCount + 1,
                    "maxCapacity" to classSchedule.maxCapacity,
                    "bookedUsers" to FieldValue.arrayUnion(currentUserId)
                )

                classBookingsRef.set(updatedBookingData, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Booked Successfully", Toast.LENGTH_SHORT).show()
                        fetchClassSchedules() // Refresh UI with updated info
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to update booking data", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to increment booking count", Toast.LENGTH_SHORT).show()
            }
    }


    private fun getImageResId(imageName: String): Int {
        return when (imageName.lowercase()) {
            "yoga" -> R.drawable.yoga
            "cardio" -> R.drawable.cardio
            "pilates" -> R.drawable.pilates
            else -> R.drawable.flexnow
        }
    }
    private fun getImageNameFromResId(resId: Int): String {
        return when (resId) {
            R.drawable.yoga -> "yoga"
            R.drawable.cardio -> "cardio"
            R.drawable.pilates -> "pilates"
              R.drawable.strenght->"strength"
            R.drawable.hiit-> "hit"
            else -> "flexnow"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
