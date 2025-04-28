package com.momin.l226955.flexnow

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // Get reference to the VideoView
        val videoView: VideoView = findViewById(R.id.videoView)

        // Get the video URI string passed from the adapter or fragment
        val videoUriString = intent.getStringExtra("videoUri")

        // Check if the URI string is not null
        if (videoUriString != null) {
            // Convert the URI string back to a URI
            val videoUri = Uri.parse(videoUriString)

            // Set the URI to the VideoView and start playing
            videoView.setVideoURI(videoUri)
            videoView.start()

            // Optionally, add listeners to handle video events (like when it finishes)
            videoView.setOnCompletionListener {
                // Handle video completion
            }
        } else {
            Toast.makeText(this, "Video not available", Toast.LENGTH_SHORT).show()
        }
    }
}

