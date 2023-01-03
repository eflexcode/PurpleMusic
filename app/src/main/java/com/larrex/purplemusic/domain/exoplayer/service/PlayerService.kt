package com.larrex.purplemusic.domain.exoplayer.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.net.toUri
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.larrex.purplemusic.MainActivity
import com.larrex.purplemusic.Util.Companion.MEDIA_ID
import com.larrex.purplemusic.domain.exoplayer.MyPlayerListener
import com.larrex.purplemusic.domain.exoplayer.MyPlayerNotificationManager
//import com.larrex.purplemusic.domain.exoplayer.Util.Companion.MEDIA_ID
import com.larrex.purplemusic.domain.exoplayer.callback.PlayerCallBack
import com.larrex.purplemusic.domain.repository.Repository
import com.larrex.purplemusic.domain.room.NextUpSongs
import com.larrex.purplemusic.domain.room.NowPlaying
import com.larrex.purplemusic.domain.room.PurpleDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerService"

@AndroidEntryPoint
class PlayerService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var repository: Repository

//    @Inject
    lateinit var player: ExoPlayer

    var isPlaying = false

    var nowPlaying: NowPlaying? = null

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var notificationManager: MyPlayerNotificationManager

    private val serviceJob = Job()
    val scope = CoroutineScope(Dispatchers.Main + serviceJob)

    var allNextUpList: List<NextUpSongs> = ArrayList<NextUpSongs>()

    companion object {
        var curDuration = 0L
            private set
    }

    val mediaItems: MutableList<MediaItem> =
        ArrayList<MediaItem>()

    init {

        CoroutineScope(Dispatchers.Main).launch {

            repository.getNextUps().collectLatest {
                player.clearMediaItems()
                mediaItems.clear()
                it.forEach {

                    mediaItems.add(MediaItem.fromUri(it.songUri.toUri()))

                }.also {

                    player.addMediaItems(mediaItems)
                }

            }

        }

        scope.launch {
            repository.getNowPlaying().collectLatest {

                nowPlaying = it
                if (it != null)
                    player.repeatMode =
                        if (it.repeat == 1) Player.REPEAT_MODE_OFF else if (it.repeat == 2) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_ONE

                if (isPlaying)
                    player.shuffleModeEnabled = it.shuffle

            }
        }

        if (nowPlaying != null) {

            player.repeatMode =
                if (nowPlaying!!.repeat == 1) Player.REPEAT_MODE_OFF else if (nowPlaying!!.repeat == 2) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_ONE

        }


        val listener = object : Player.Listener {

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)

                try {


                    if (mediaItems.isNotEmpty()) {

                        val id = nowPlaying?.id

                        val musicUri = allNextUpList[player.currentMediaItemIndex].songUri
                        val musicName = allNextUpList[player.currentMediaItemIndex].songName
                        val artistName = allNextUpList[player.currentMediaItemIndex].artistName
                        val albumArt = allNextUpList[player.currentMediaItemIndex].songCoverImageUri
                        val duration =
                            allNextUpList[player.currentMediaItemIndex].duration.toFloat()

                        Log.d(
                            TAG,
                            "onMediaItemTransition: 44 ${allNextUpList[player.currentMediaItemIndex].songName}"
                        )

                        if (isPlaying) {

                            scope.launch {

                                Log.d(TAG, "onMediaItemTransition id: $id")
                                if (id != null) {
                                    Log.d(TAG, "onMediaItemTransition id2: $id")

                                    repository.updateNowPlaying(
                                        id,
                                        musicUri,
                                        musicName,
                                        artistName,
                                        albumArt,
                                        duration,
                                    )


                                }

                            }

                        }
                    }
                } catch (e: Exception) {
//                    play()
                }
            }

            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)

            }

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                super.onTimelineChanged(timeline, reason)
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)

            }


        }

        player.addListener(listener)

        notificationManager.showNotificationForPlayer(player)
    }

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



        sessionToken = mediaSession.sessionToken
        notificationManager =
            MyPlayerNotificationManager(this, mediaSession.sessionToken, MyPlayerListener(this)) {
                curDuration = player.duration
            }
//        mediaSession.setCallback(PlayerCallBack(exoPlayer,mediaSession,this,mediaItems[0]))

        mediaSessionConnector.setPlayer(player)

        CoroutineScope(Dispatchers.IO).launch {

            repository.getNextUps().collectLatest {

                it.forEach {

                    val descriptionCompat = MediaDescriptionCompat.Builder().setTitle(it.songName)
                        .setSubtitle(it.artistName).setIconUri(it.songCoverImageUri.toUri())
                        .setMediaUri(it.songUri.toUri()).setMediaId(it.id.toString()).build()
//
//                    mediaItems.add(
//                        MediaBrowserCompat.MediaItem(
//                            descriptionCompat, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
//                        )
//                    )

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

                    result.sendResult(null)

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



























