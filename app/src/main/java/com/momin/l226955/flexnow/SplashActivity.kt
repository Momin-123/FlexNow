package com.momin.l226955.flexnow


import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.momin.l226955.flexnow.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the image view or layout that you want to animate
        val imageView: ImageView = binding.splashImage

        // Load the zoom-in animation from the 'anim' folder
        val zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)

        // Start the animation on the image view
        imageView.startAnimation(zoomInAnimation)

        // Move to the next activity after the animation ends
        zoomInAnimation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {
                // No action needed on start
            }

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                // Proceed to the next activity after the animation ends
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish() // Close SplashActivity
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {
                // No action needed on repeat
            }
        })
    }
}

