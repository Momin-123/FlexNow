package com.momin.l226955.flexnow.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.momin.l226955.flexnow.databinding.FragmentMyBookingsBinding

class MyBookingsFragment : Fragment() {

    private var _binding: FragmentMyBookingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookingAdapter: MyBookingsAdapter
    private var bookedClassList = mutableListOf<Map<String, Any>>()
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

        fetchUserBookings()

        bookingAdapter = MyBookingsAdapter(
            bookedClassList,
            onCancelBookingClick = { classData -> cancelBooking(classData) },
            onJoinClassClick = { classData -> joinClass(classData) }
        )

        binding.recyclerViewMyBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMyBookings.adapter = bookingAdapter
    }

    private fun fetchUserBookings() {
        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            db.collection("class_bookings")
                .whereArrayContains("bookedUsers", currentUserId)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(requireContext(), "No bookings found", Toast.LENGTH_SHORT).show()
                    } else {
                        bookedClassList.clear()
                        for (doc in documents) {
                            val classData = doc.data
                            bookedClassList.add(classData)
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

    private fun cancelBooking(classData: Map<String, Any>) {
        val currentUserId = auth.currentUser?.uid
        val className = classData["className"] as? String ?: return
        val bookingsCount = (classData["bookingsCount"] as? Long)?.toInt() ?: return

        if (currentUserId != null) {
            db.collection("class_bookings").document(className)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val bookedUsers = document.get("bookedUsers") as? List<String> ?: emptyList()
                        val updatedBookedUsers = bookedUsers.toMutableList().apply {
                            remove(currentUserId)
                        }

                        val updatedClassData = hashMapOf(
                            "bookedUsers" to updatedBookedUsers,
                            "bookingsCount" to (bookingsCount - 1).coerceAtLeast(0)
                        )

                        db.collection("class_bookings").document(className)
                            .update(updatedClassData)
                            .addOnSuccessListener {
                                val classDocRef = db.collection("Classes").document(className)
                                db.runTransaction { transaction ->
                                    val snapshot = transaction.get(classDocRef)
                                    val currentCount = snapshot.getLong("bookingsCount") ?: 0
                                    transaction.update(classDocRef, "bookingsCount", (currentCount - 1).coerceAtLeast(0))
                                }.addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Booking Cancelled: $className", Toast.LENGTH_SHORT).show()
                                    bookedClassList.remove(classData)
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

    private fun joinClass(classData: Map<String, Any>) {
        val className = classData["className"] as? String ?: return

        db.collection("class_bookings").document(className)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val classLink = document.getString("classLink")
                    if (!classLink.isNullOrEmpty()) {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(classLink))
                            startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Unable to open link", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "No class link available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Class not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch class link", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
