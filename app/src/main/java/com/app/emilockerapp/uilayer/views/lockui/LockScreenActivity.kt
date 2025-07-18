package com.app.emilockerapp.uilayer.views.lockui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class LockScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContent {
            LockScreenUI(
                onAppClick = {
//                    setSkipLockOnce(this, true)
//                    setEMILocked(this, false)
//                    val lockIntent = Intent(this, MainActivity::class.java)
//                    lockIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    startActivity(lockIntent)
//                    finish()
                },
                onEmergencyClick = {
                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        data = Uri.parse("tel:")
                    }

                    try {
                        startActivity(dialIntent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(this, "No dialer app found", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // Relaunch lock screen on Home button
        val intent = Intent(this, LockScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
