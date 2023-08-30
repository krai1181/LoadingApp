package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.MainActivity
import com.udacity.R

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0


fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    // Create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    
    
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext, applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_downloading).setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    

   // notify(NOTIFICATION_ID, builder.build())

}


fun NotificationManager.cancelNotifications() {
    this.cancelAll()
}