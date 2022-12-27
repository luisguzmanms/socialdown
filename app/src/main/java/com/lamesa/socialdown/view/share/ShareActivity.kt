package com.lamesa.socialdown.view.share

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lamesa.socialdown.view.main.MainActivity

class ShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when {intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    handleSendText(intent) // Handle text being sent
                }
            } else -> {
                // Handle other intents, such as being started from the home screen
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            // Update UI to reflect text being shared
            val intentMainActivity = Intent(this, MainActivity::class.java)
            intentMainActivity.putExtra("link", it)
            startActivity(intentMainActivity)
        }
    }

}