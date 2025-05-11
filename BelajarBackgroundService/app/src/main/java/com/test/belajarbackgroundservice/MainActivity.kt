package com.test.belajarbackgroundservice

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.test.belajarbackgroundservice.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d(TAG, if (isGranted) "Notification permission granted" else "Notification permission denied")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkNotificationPermission()

        binding.timeText.text = "00:00:00"

        binding.startButton.setOnClickListener {
            val intent = Intent(this, TimerService::class.java).apply {
                action = TimerService.ACTION_START
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        binding.stopButton.setOnClickListener {
            val intent = Intent(this, TimerService::class.java).apply {
                action = TimerService.ACTION_STOP
            }
            startService(intent)
        }

        binding.resetButton.setOnClickListener {
            val intent = Intent(this, TimerService::class.java).apply {
                action = TimerService.ACTION_RESET
            }
            startService(intent)
            binding.timeText.text = "00:00:00"
        }

        observeTimerState()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    Log.d(TAG, "Notification permission already granted")
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun observeTimerState() {
        lifecycleScope.launch {
            TimerState.timeInSeconds.collectLatest { seconds ->
                val formatted = formatTime(seconds)
                binding.timeText.text = formatted
                Log.d(TAG, "Time updated in UI: $formatted")
            }
        }

        val updateIntent = Intent(this, TimerService::class.java).apply {
            action = TimerService.ACTION_UPDATE
        }
        startService(updateIntent)
    }

    private fun formatTime(seconds: Int): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
