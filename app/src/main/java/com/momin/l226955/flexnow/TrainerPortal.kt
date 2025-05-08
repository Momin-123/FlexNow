package com.momin.l226955.flexnow

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class TrainerPortal : AppCompatActivity() {

    private lateinit var classTitleEditText: EditText
    private lateinit var classTypeEditText: EditText
    private lateinit var classTimeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var logoutButton: Button
    private lateinit var createScheduleButton: Button
    private lateinit var uploadDescriptionButton: Button
    private lateinit var uploadVideoButton: Button
    private lateinit var uploadPhotoButton: Button
    private lateinit var classLinkEditText: EditText
    private lateinit var uploadClassLinkButton: Button

    private val db = FirebaseFirestore.getInstance()
    private val rtdb = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var profileImageView: ImageView
    private val VIDEO_PICK_CODE = 101
    private val PHOTO_PICK_CODE = 102
    private var profileImageUri: Uri? = null
    private var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainer_portal)
        classLinkEditText = findViewById(R.id.classLinkEditText)
        uploadClassLinkButton = findViewById(R.id.uploadLinkButton)

        classTitleEditText = findViewById(R.id.class_title)
        classTypeEditText = findViewById(R.id.class_type)
        classTimeEditText = findViewById(R.id.class_time)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        logoutButton = findViewById(R.id.logoutButton)
        profileImageView = findViewById(R.id.profileImageView)
        createScheduleButton = findViewById(R.id.create_schedule_button)
        uploadDescriptionButton = findViewById(R.id.uploadDescriptionButton)
        uploadVideoButton = findViewById(R.id.uploadWorkoutVideoButton)
        uploadPhotoButton = findViewById(R.id.uploadProfilePhotoButton)
        uploadClassLinkButton.setOnClickListener {
            val classLink = classLinkEditText.text.toString().trim()
            val userId = auth.currentUser?.uid

            if (classLink.isEmpty()) {
                Toast.makeText(this, "Please enter a class link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userId == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fetchTrainerNameById(userId) { trainerName ->
                if (trainerName == null) {
                    Toast.makeText(this, "Trainer name not found", Toast.LENGTH_SHORT).show()
                    return@fetchTrainerNameById
                }

                db.collection("class_bookings").get()
                    .addOnSuccessListener { querySnapshot ->
                        var found = false
                        for (document in querySnapshot.documents) {
                            val docInstructorName = document.getString("instructorName")
                            if (docInstructorName == trainerName) {
                                // Correct field name here
                                val updateMap = mapOf("classLink" to classLink)
                                db.collection("class_bookings").document(document.id)
                                    .set(updateMap, SetOptions.merge())
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Class link added!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                found = true
                                break
                            }
                        }
                        if (!found) {
                            Toast.makeText(this, "No matching class found for this trainer", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error fetching classes: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }


        createScheduleButton.setOnClickListener {
            val title = classTitleEditText.text.toString().trim()
            val type = classTypeEditText.text.toString().trim()
            val time = classTimeEditText.text.toString().trim()
            val userId = auth.currentUser?.uid

            if (title.isNotEmpty() && type.isNotEmpty() && time.isNotEmpty() && userId != null) {
                fetchTrainerNameById(userId) { name ->
                    if (name != null) {
                        val schedule = mapOf(
                            "bookingsCount" to 0,
                            "className" to title,
                            "imageName" to title,
                            "instructorName" to name,
                            "maxCapacity" to 20,
                            "time" to time
                        )
                        db.collection("Classes").document(title).delete().addOnCompleteListener {
                            db.collection("Classes").document(title).set(schedule)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Class schedule created!", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(this, "Failed to create schedule.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Trainer name not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        uploadDescriptionButton.setOnClickListener {
            val title = classTitleEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val userId = auth.currentUser?.uid ?: return@setOnClickListener

            if (title.isNotEmpty() && description.isNotEmpty()) {
                rtdb.child("trainers").child(userId).child("description").setValue(description)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Description uploaded.", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Upload failed.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Fill class title and description.", Toast.LENGTH_SHORT).show()
            }
        }

        uploadVideoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            startActivityForResult(intent, VIDEO_PICK_CODE)
        }

        uploadPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PHOTO_PICK_CODE)
        }
        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                PHOTO_PICK_CODE -> {
                    profileImageUri = data.data
                    profileImageView.setImageURI(profileImageUri)
                    Toast.makeText(this, "Profile image selected", Toast.LENGTH_SHORT).show()
                }
                VIDEO_PICK_CODE -> {
                    videoUri = data.data
                    Toast.makeText(this, "Video selected (not uploading)", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    private fun fetchTrainerNameById(trainerId: String, callback: (String?) -> Unit) {
        rtdb.child("trainers").child(trainerId).child("username")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot.getValue(String::class.java))
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }
}
