package com.udacity.util

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.R

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0


@SuppressLint("SuspiciousIndentation")
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, contentIntent: Intent) {

    // Create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE
    )
    
    
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext, applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_downloading,
            applicationContext.getString(R.string.notification_button), contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    
        notify(NOTIFICATION_ID, builder.build())

}


fun NotificationManager.cancelNotifications() {
    this.cancelAll()
}
