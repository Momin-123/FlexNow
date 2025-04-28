data class RecordedWorkout(
    val thumbnailResId: Int,  // Resource ID for the thumbnail image
    val instructorName: String,
    val uploadDate: String,  // Changed 'date' to 'uploadDate'
    val workoutName: String, // Changed 'title' to 'workoutName'
    val videoUrl: String     // Changed 'videoResId' to 'videoUrl' to handle both raw and online videos
)
