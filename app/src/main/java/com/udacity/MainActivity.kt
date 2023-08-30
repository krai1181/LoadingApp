package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var customButton: LoadingButton
    
    private var baseURL = ""

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        
        customButton = binding.mainContent.customButton

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        // TODO: Implement code below
       customButton.setOnClickListener {
           customButton.hasDownloadingCompleted()
           download()

       }
            
            binding.mainContent.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId -> 
              baseURL =  when(checkedId){
                    R.id.glide_radio_btn -> GLIDE_URL
                    R.id.load_app_radio_btn -> URL
                    else -> RETROFIT_URL
              }
            }
            
            
          
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
      
     val toast =  Toast.makeText(customButton.context, resources.getString(R.string.toast_message), Toast.LENGTH_LONG)
     //toast.setGravity(Gravity.TOP or Gravity.START, x, y)

     toast.show()    
    }

    companion object {
        private const val URL = 
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide"
        private const val RETROFIT_URL = "https://github.com/square/retrofit"
        private const val CHANNEL_ID = "channelId"
    }
}