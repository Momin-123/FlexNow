package com.momin.l226955.flexnow.ui

data class ClassSchedule(
    val id: String = "",
    val imageResId: Int = 0,
    val time: String = "",
    val instructorName: String = "",
    val className: String = "",
    var bookingsCount: Int = 0,
    val maxCapacity: Int = 20, // default max capacity
    var isBooked: Boolean = false
)
