package com.example.weatherforcasting.ui.details

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherforcasting.R
import com.example.weatherforcasting.data.model.NotificationData
import com.example.weatherforcasting.databinding.ActivityDetailsBinding
import com.example.weatherforcasting.util.NotificationWorker
import com.google.android.material.snackbar.Snackbar
import com.linkme.cartiapp.util.GetObjectGson.getNotificationData
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViews(getNotificationData(intent.getStringExtra(ITEM_INFO)?:""))// get info
    }

    private fun setViews(notificationData: NotificationData?) {
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay((notificationData?.time?:"0").toLong(), TimeUnit.SECONDS)
            .addTag(notificationData?.id.toString())
            .build()

        binding.cancelButton.setOnClickListener{
            // cancel the work manager with that id
            WorkManager.getInstance(this).cancelAllWorkByTag(notificationData?.id.toString())
            Snackbar.make(binding.root, "the notification is canceled", Snackbar.LENGTH_SHORT).show()

        }
        binding.scheduleButton.setOnClickListener{
            WorkManager.getInstance(this).enqueue(workRequest)
            Snackbar.make(binding.root, "the notification is scheduled", Snackbar.LENGTH_SHORT).show()

        }
    }

    companion object{
        const val ITEM_INFO = "itemInfo"
    }

}