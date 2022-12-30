package com.kenil.gallaryviewer.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.kenil.gallaryviewer.R
import com.kenil.gallaryviewer.databinding.ActivityHomeBinding
import com.kenil.gallaryviewer.databinding.ActivitySplashBinding
import com.kenil.gallaryviewer.views.utils.StartActivity

class SplashActivity : AppCompatActivity() {
    private var binding: ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTopLayer)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Handler().postDelayed(Runnable {
            this.StartActivity(HomeActivity(), finishActivity = true)
        }, 500)
    }

}