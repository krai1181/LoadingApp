package com.udacity.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.udacity.R
import com.udacity.util.sendNotification

class DownloadReceiver: BroadcastReceiver() {
    
    override fun onReceive(context: Context, p1: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(context.getString(R.string.notification_description), context)
    }
}
