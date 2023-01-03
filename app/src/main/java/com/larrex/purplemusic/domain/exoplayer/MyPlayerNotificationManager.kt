package com.larrex.purplemusic.domain.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util.Companion.CHANNEL_ID
import com.larrex.purplemusic.Util.Companion.Notification_ID

class MyPlayerNotificationManager(
    private val context: Context,
    session: MediaSessionCompat.Token,
    playerListener: NotificationListener,
    private val newSongCallback: () -> Unit
) {

    private val playerNotificationManager: PlayerNotificationManager

    init {

        val mediaController = MediaControllerCompat(context, session)

        playerNotificationManager =
            PlayerNotificationManager.Builder(context, Notification_ID, CHANNEL_ID)
                .setNotificationListener(playerListener)
                .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
                .setChannelNameResourceId(R.string.notification_channel)
                .setChannelDescriptionResourceId(R.string.notification_channel_description)
                .build()

        playerNotificationManager.setMediaSessionToken(session)
        playerNotificationManager.setSmallIcon(R.drawable.ic_artist)
        playerNotificationManager.setUsePlayPauseActions(true)
        playerNotificationManager.setUsePreviousAction(true)
        playerNotificationManager.setUseNextAction(true)

    }

    fun hideNotification() {
        playerNotificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player){
        playerNotificationManager.setPlayer(player)
    }

    private inner class DescriptionAdapter(private val mediaControllerCompat: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {

        override fun getCurrentContentTitle(player: Player): CharSequence {
            newSongCallback()
            return mediaControllerCompat.metadata.description.title.toString()
        }


        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaControllerCompat.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return mediaControllerCompat.metadata.description.subtitle.toString()
        }


        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            Glide.with(context).asBitmap()
                .load(mediaControllerCompat.metadata.description.iconUri)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        callback.onBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) = Unit
                })
            return null
        }

    }

}