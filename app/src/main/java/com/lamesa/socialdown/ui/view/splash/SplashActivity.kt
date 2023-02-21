package com.lamesa.socialdown.ui.view.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.lamesa.socialdown.databinding.ActivitySplashBinding
import com.lamesa.socialdown.utils.DialogXUtils

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Comprobrar que el usuario acepte las politicas de privacidad
        Handler().postDelayed({
            DialogXUtils.Policies.dialogPrivacyPolicies(this)
        }, 1500)

    }

}