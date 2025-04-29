package com.momin.l226955.flexnow.ui

data class ClassSchedule(
    val imageResId: Int,
    val time: String,
    val instructorName: String,
    val className: String,
    var bookingsCount: Int,
    var isBooked: Boolean = false  // New field to track booking status
)
