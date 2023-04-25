package com.larrex.purplemusic.domain.exoplayer

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener
import com.larrex.purplemusic.Util.Companion.Notification_ID
import com.larrex.purplemusic.domain.exoplayer.service.PlayerService

class MyPlayerListener(val playerService: PlayerService,) : NotificationListener {


    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)

        playerService.apply {

            stopForeground(true)
//            isPlaying = false
            stopSelf()

        }

    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)

        playerService.apply {
//            if (ongoing && !isPlaying) {
//
//                ContextCompat.startForegroundService(
//                    this,
//                    Intent(applicationContext, this::class.java)
//                )
//
//                startForeground(Notification_ID, notification)
//
////                isPlaying = true
//            }
        }
    }
}