package com.larrex.purplemusic.domain.exoplayer.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.net.toUri
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.larrex.purplemusic.MainActivity
import com.larrex.purplemusic.Util.Companion.MEDIA_ID
import com.larrex.purplemusic.domain.exoplayer.MyPlayerListener
import com.larrex.purplemusic.domain.exoplayer.MyPlayerNotificationManager
//import com.larrex.purplemusic.domain.exoplayer.Util.Companion.MEDIA_ID
import com.larrex.purplemusic.domain.exoplayer.callback.PlayerCallBack
import com.larrex.purplemusic.domain.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var repository: Repository

//    @Inject
//    lateinit var exoPlayer: SimpleExoPlayer

     var isPlaying = false

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var notificationManager: MyPlayerNotificationManager

    companion object {
        var curDuration = 0L
            private set
    }

    val mediaItems: MutableList<MediaBrowserCompat.MediaItem> =
        ArrayList<MediaBrowserCompat.MediaItem>()

    override fun onCreate() {
        super.onCreate()

//        val desc = MediaDescriptionCompat.Builder()
//            .setMediaUri(song.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri())
//            .setTitle(song.description.title)
//            .setSubtitle(song.description.subtitle)
//            .setMediaId(song.description.mediaId)
//            .setIconUri(song.description.iconUri)
//            .build()
//        MediaBrowserCompat.MediaItem(desc, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)


        val mainActivityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, 0)

        mediaSession = MediaSessionCompat(this, "PlayerService")
        mediaSession.setSessionActivity(pendingIntent)
        mediaSession.isActive = true
        mediaSessionConnector = MediaSessionConnector(mediaSession)

//        mediaSessionConnector.setEnabledPlaybackActions(
//            PlaybackStateCompat.ACTION_PLAY_PAUSE
//                    or PlaybackStateCompat.ACTION_PLAY
//                    or PlaybackStateCompat.ACTION_PAUSE
//                    or PlaybackStateCompat.ACTION_SEEK_TO
//                    or PlaybackStateCompat.ACTION_FAST_FORWARD
//                    or PlaybackStateCompat.ACTION_REWIND
//                    or PlaybackStateCompat.ACTION_STOP
//                    or PlaybackStateCompat.ACTION_SET_REPEAT_MODE
//                    or PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE
//        )

        sessionToken = mediaSession.sessionToken
        notificationManager = MyPlayerNotificationManager(this,mediaSession.sessionToken,MyPlayerListener(this)  ) {
//            curDuration = exoPlayer.duration
        }
//        mediaSession.setCallback(PlayerCallBack(exoPlayer,mediaSession,this,mediaItems[0]))

//        mediaSessionConnector.setPlayer(exoPlayer)

        CoroutineScope(Dispatchers.IO).launch {

            repository.getNextUps().collectLatest {

                it.forEach {

                    val descriptionCompat = MediaDescriptionCompat.Builder().setTitle(it.songName)
                        .setSubtitle(it.artistName).setIconUri(it.songCoverImageUri.toUri())
                        .setMediaUri(it.songUri.toUri()).setMediaId(it.id.toString()).build()

                    mediaItems.add(
                        MediaBrowserCompat.MediaItem(
                            descriptionCompat, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                        )
                    )

                }



            }
            repository.getNowPlaying().collectLatest {


            }
        }

    }

    override fun onGetRoot(
        clientPackageName: String, clientUid: Int, rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(MEDIA_ID, null)
    }

    override fun onLoadChildren(
        parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {

        when (parentId) {

            MEDIA_ID -> {

                if (mediaItems.isNotEmpty()) {

                    result.sendResult(mediaItems)

                    if (!isPlaying)

                        doPlayMusic()
                    else result.sendResult(null)

                } else {
                    result.detach()
                }

            }

        }

    }

    private fun doPlayMusic() {


    }


}



























