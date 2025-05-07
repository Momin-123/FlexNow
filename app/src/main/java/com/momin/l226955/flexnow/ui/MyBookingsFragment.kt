package com.momin.l226955.flexnow.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.momin.l226955.flexnow.R
import com.momin.l226955.flexnow.databinding.FragmentMyBookingsBinding

class MyBookingsFragment : Fragment() {

    private var _binding: FragmentMyBookingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookingAdapter: MyBookingsAdapter
    private var bookedClassList = mutableListOf<ClassSchedule>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch the bookings for the current user
        fetchUserBookings()

        // Set up the RecyclerView with the booking adapter
        bookingAdapter = MyBookingsAdapter(bookedClassList,
            onCancelBookingClick = { classSchedule ->
                // Handle Cancel Booking button click
                cancelBooking(classSchedule)
            },
            onJoinClassClick = { classSchedule ->
                // Handle Join Class button click
                joinClass(classSchedule)
            }
        )

        binding.recyclerViewMyBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMyBookings.adapter = bookingAdapter
    }

    private fun fetchUserBookings() {
        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            db.collection("class_bookings")
                .whereArrayContains("bookedUsers", currentUserId)  // Query for classes that have the current user in bookedUsers
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(requireContext(), "No bookings found", Toast.LENGTH_SHORT).show()
                    } else {
                        bookedClassList.clear()
                        for (doc in documents) {
                            // Log the document data to verify it's correct
                            Log.d("FirestoreData", "Fetched document: ${doc.data}")

                            val className = doc.getString("className") ?: ""
                            val instructorName = doc.getString("instructorName") ?: ""
                            val time = doc.getString("time") ?: ""
                            val bookingsCount = doc.getLong("bookingsCount")?.toInt() ?: 0
                            val maxCapacity = doc.getLong("maxCapacity")?.toInt() ?: 20
                            val imageName = doc.getString("imageName") ?: ""

                            val classSchedule = ClassSchedule(
                                imageResId = getImageResId(imageName),
                                time = time,
                                instructorName = instructorName,
                                className = className,
                                bookingsCount = bookingsCount,
                                maxCapacity = maxCapacity,
                                isBooked = true  // Assuming this is a booked class for the user
                            )
                            bookedClassList.add(classSchedule)
                        }
                        bookingAdapter.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to fetch bookings", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelBooking(classSchedule: ClassSchedule) {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            val className = classSchedule.className

            db.collection("class_bookings").document(className)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val bookedUsers = document.get("bookedUsers") as? List<String> ?: emptyList()
                        val updatedBookedUsers = bookedUsers.toMutableList().apply {
                            remove(currentUserId)  // Remove the current user from the bookedUsers list
                        }

                        // Update the Firestore document with the updated bookedUsers and bookingsCount
                        val updatedClassData = hashMapOf(
                            "bookedUsers" to updatedBookedUsers,
                            "bookingsCount" to (classSchedule.bookingsCount - 1).coerceAtLeast(0)
                        )

                        db.collection("class_bookings").document(className)
                            .update(updatedClassData)
                            .addOnSuccessListener {
                                // Decrement bookingsCount in Classes collection
                                val classDocRef = db.collection("Classes").document(className)
                                db.runTransaction { transaction ->
                                    val snapshot = transaction.get(classDocRef)
                                    val currentCount = snapshot.getLong("bookingsCount") ?: 0
                                    transaction.update(classDocRef, "bookingsCount", (currentCount - 1).coerceAtLeast(0))
                                }.addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Booking Cancelled: ${classSchedule.className}", Toast.LENGTH_SHORT).show()
                                    bookedClassList.remove(classSchedule)  // Remove from list
                                    bookingAdapter.notifyDataSetChanged()
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(), "Failed to update class info", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Failed to cancel booking", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
        }
    }

    private fun joinClass(classSchedule: ClassSchedule) {
        // Implement your join class logic here (e.g., open Zoom link)
        Toast.makeText(requireContext(), "Joining Class: ${classSchedule.className}", Toast.LENGTH_SHORT).show()
    }

    private fun getImageResId(imageName: String): Int {
        return when (imageName.lowercase()) {
            "yoga" -> R.drawable.yoga
            "cardio" -> R.drawable.cardio
            "pilates" -> R.drawable.pilates
            "strength" -> R.drawable.strenght
            "hit" -> R.drawable.hiit
            else -> R.drawable.flexnow
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
