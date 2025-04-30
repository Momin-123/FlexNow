package com.momin.l226955.flexnow

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var fullscreenToggle: ImageButton
    private var isFullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        videoView = findViewById(R.id.videoView)
        fullscreenToggle = findViewById(R.id.fullscreenToggle)

        val videoUriString = intent.getStringExtra("videoUri")
        if (videoUriString != null) {
            val videoUri = Uri.parse(videoUriString)
            val mediaController = MediaController(this)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(videoUri)
            videoView.start()
        } else {
            Toast.makeText(this, "Video not available", Toast.LENGTH_SHORT).show()
        }

        fullscreenToggle.setOnClickListener {
            if (isFullscreen) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                fullscreenToggle.setImageResource(R.drawable.fullscreenbutton)
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                fullscreenToggle.setImageResource(R.drawable.fullscreenbuttonexit)
            }
            isFullscreen = !isFullscreen
        }
    }
}
