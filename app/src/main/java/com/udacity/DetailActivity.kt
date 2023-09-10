package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.util.cancelNotifications

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val repositoriesMap = mapOf(
             MainActivity.GLIDE_URL to resources.getString(R.string.glide_image_library) ,
             MainActivity.URL to resources.getString(R.string.load_app_title),
             MainActivity.RETROFIT_URL to resources.getString(R.string.retrofit_title) 
        )

        val downloadedRepositoryName = intent.getStringExtra(repositoryNameKey)
        binding.detailContent.fileNameContent.text = repositoriesMap[downloadedRepositoryName]

        val downloadedRepositoryStatus = intent.getStringExtra(repositoryStatusKey)
        binding.detailContent.statusContent.text = downloadedRepositoryStatus

        binding.detailContent.okBtn.setOnClickListener {
            this.finish()
        }

        val notificationManager = ContextCompat.getSystemService(this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotifications()
    }
}
