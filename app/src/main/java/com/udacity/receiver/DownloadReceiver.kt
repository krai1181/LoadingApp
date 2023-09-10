package com.udacity.receiver

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.udacity.DetailActivity
import com.udacity.R
import com.udacity.repositoryNameKey
import com.udacity.repositoryStatusKey
import com.udacity.util.sendNotification


class DownloadReceiver: BroadcastReceiver() {
    
    
    @SuppressLint("Range")
    override fun onReceive(context: Context, intent: Intent) {
        val contentIntent = Intent(context, DetailActivity::class.java)
        
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val action = intent.action
        if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val query = DownloadManager.Query()
            query.setFilterById(id)
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val cursor = manager.query(query)
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_URI)
                val uriString = cursor.getString(columnIndex)
                val uri = Uri.parse(uriString)
                
                contentIntent.putExtra(repositoryNameKey, uri.toString())
                if (cursor.count >= 0) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (DownloadManager.STATUS_SUCCESSFUL == status) {
                        contentIntent.putExtra(repositoryStatusKey, context
                            .getString(R.string.downloading_status_success))
                    }
                    if (DownloadManager.STATUS_FAILED == status) {
                        contentIntent.putExtra(repositoryStatusKey, context
                            .getString(R.string.downloading_status_failed))
                    }
                }
            }
        }
        
        notificationManager.sendNotification(context
            .getString(R.string.notification_description), context, contentIntent)
    }
}


