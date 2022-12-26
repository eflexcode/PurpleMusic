package com.larrex.purplemusic.domain.exoplayer.callback

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media.session.MediaButtonReceiver
import com.google.android.exoplayer2.ExoPlayer
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util


class PlayerCallBack(
    val player: ExoPlayer,
    val mediaSession: MediaSessionCompat,
    val context: Context,
    val item: MediaBrowserCompat.MediaItem
) : MediaSessionCompat.Callback() {


    override fun onPrepare() {
        super.onPrepare()
    }


    override fun onPlay() {
        super.onPlay()

        val bitmap = MediaStore.Images.Media.getBitmap(
            context.getContentResolver(),
            item.description.iconUri
        )

        val noti = NotificationCompat.Builder(context, Util.CHANNEL_ID).apply {

            setContentTitle(item.description.title)
            setContentText(item.description.subtitle)
            setLargeIcon(bitmap)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSmallIcon(R.mipmap.ic_launcher_round)

            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_round_skip_backward,
                    "Previous",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    )
                )
            )
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_round_play,
                    "Pause play",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
                    )
                )
            )
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_round_skip_forward,
                    "Next",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    )
                )
            )


        }



    }


    override fun onSkipToQueueItem(id: Long) {
        super.onSkipToQueueItem(id)
    }


    override fun onPause() {
        super.onPause()
    }


    override fun onSkipToNext() {
        super.onSkipToNext()
    }


    override fun onSkipToPrevious() {
        super.onSkipToPrevious()
    }


    override fun onStop() {
        super.onStop()
    }

    override fun onSeekTo(pos: Long) {
        super.onSeekTo(pos)
    }

    /**
     * Override to handle media button events.
     *
     *
     * The double tap of [KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE] or [ ][KeyEvent.KEYCODE_HEADSETHOOK] will call the [.onSkipToNext] by default. If the
     * current SDK level is 27 or higher, the default double tap handling is done by framework
     * so this method would do nothing for it.
     *
     * @param mediaButtonEvent The media button event intent.
     * @return True if the event was handled, false otherwise.
     */
    override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
        return super.onMediaButtonEvent(mediaButtonEvent)
    }


}