package com.larrex.purplemusic

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyHiltApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Util.CHANNEL_NAME,
                Util.CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager =  getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChannel)

        }
    }
}


