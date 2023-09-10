package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.util.sendNotification


const val repositoryNameKey = "DownloadedRepositoryNameKey"
 const val repositoryStatusKey = "DownloadedRepositoryStatusKey"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var customButton: LoadingButton
    private lateinit var toast: Toast

    private var baseURL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        customButton = binding.mainContent.customButton

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        customButton.setOnClickListener {
            customButton.hasDownloadingCompleted()
            download()

        }

        binding.mainContent.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            baseURL = when (checkedId) {
                R.id.glide_radio_btn -> GLIDE_URL
                R.id.load_app_radio_btn -> URL
                else -> RETROFIT_URL
            }
        }

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_title)
        )

        val layout = layoutInflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container))
        val toastMessage = layout.findViewById<TextView>(R.id.toast_msg)
        toastMessage.text = getString(R.string.toast_message)
        toast = Toast(applicationContext)
        toast.setGravity(Gravity.BOTTOM, 0, 50)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {
        if (baseURL.isEmpty()) {
            showToast()
            return
        }
        val request =
            DownloadManager.Request(Uri.parse(baseURL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun showToast() {
        toast.show()
    }

    private fun sendNotification(intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(getString(R.string.notification_description), this, intent)
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description = resources.getString(R.string.notification_description)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    companion object {
        const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        const val GLIDE_URL = "https://github.com/bumptech/glide"
        const val RETROFIT_URL = "https://github.com/square/retrofit"
        private const val CHANNEL_ID = "channelId"
    }
}