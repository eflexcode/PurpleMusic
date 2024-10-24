package com.larrex.purplemusic.domain.exoplayer.service

//import com.larrex.purplemusic.domain.exoplayer.Util.Companion.MEDIA_ID
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaMetadata
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.larrex.purplemusic.R
import com.larrex.purplemusic.Util.Companion.CHANNEL_NAME
import com.larrex.purplemusic.Util.Companion.Notification_ID
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.repository.Repository
import com.larrex.purplemusic.domain.room.NextUpSongs
import com.larrex.purplemusic.domain.room.NowPlaying
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerService"

@AndroidEntryPoint
class PlayerService : Service() {

    val scope = CoroutineScope(Dispatchers.Main)
    val scope2 = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var player: ExoPlayer

    var isPlaying = false
    var isPrepared = false

    val mediaItems: MutableList<MediaItem> = ArrayList<MediaItem>()
    var allNextUpList: List<NextUpSongs> = ArrayList<NextUpSongs>()
    var allNextUpList2: List<NextUpSongs> = ArrayList<NextUpSongs>()
    var allSongsList: List<SongItem> = ArrayList<SongItem>()
    var currentDuration by mutableStateOf(0L)

//    val viewModel = ViewModelProvider(ge).get(MusicViewModel::class.java)

    var nowPlaying: NowPlaying? = null

    companion object {
        var playerServiceInstance: PlayerService? = null
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        playerServiceInstance = this
    }

    override fun onDestroy() {
        updateIsPlaying(nowPlaying?.id, false)
        super.onDestroy()
    }

    private val prev: String = "Prev"
    private val playPause: String = "playPause"
    val next: String = "next"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {

            repository.getNowPlaying().collectLatest {

                if (it != null) {

                    nowPlaying = it
                    isPlaying = it.isPlaying

                    player.repeatMode =
                        if (it.repeat == 1) Player.REPEAT_MODE_OFF else if (it.repeat == 2) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_ONE

                    if (isPlaying)
                        player.shuffleModeEnabled = it.shuffle
                }
            }
        }

        if (nowPlaying != null) {

            player.repeatMode =
                if (nowPlaying!!.repeat == 1) Player.REPEAT_MODE_OFF else if (nowPlaying!!.repeat == 2) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_ONE

        }

        CoroutineScope(Dispatchers.IO).launch {

            repository.getAllSongs().collectLatest {
                allSongsList = it
            }

            repository.getNextUps().collectLatest {
                allNextUpList = it
//                println("nextup sizeeeeeeeeeee "+it.size)
            }

        }

        //get latest next ups
        CoroutineScope(Dispatchers.Main).launch {

//            repository.getNextUps().collectLatest {
//                player.clearMediaItems()
//                mediaItems.clear()
//
//                it.forEach {
//
//                    mediaItems.add(MediaItem.fromUri(it.songUri.toUri()))
//
//                }.also {
//
//                    player.addMediaItems(mediaItems)
////                    player.seekToDefaultPosition(9)
//
//                }
//
//            }

        }

        val listener = object : Player.Listener {

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)

                try {
                    if (allNextUpList2.isNotEmpty()) {


//                        if (isPlaying) {
//                    if (player.mediaItemCount > 0) {

                        val id = nowPlaying?.id

                        val musicUri = allNextUpList2[player.currentMediaItemIndex].songUri
                        val musicName = allNextUpList2[player.currentMediaItemIndex].songName
                        val artistName = allNextUpList2[player.currentMediaItemIndex].artistName
                        val albumArt =
                            allNextUpList2[player.currentMediaItemIndex].songCoverImageUri
                        val duration =
                            allNextUpList2[player.currentMediaItemIndex].duration.toFloat()

//                    println("current exop indexxxxxxxxxxxxxxxxxxx "+player.currentMediaItemIndex)
                        scope.launch {

                            if (id != null) {

                                updateNowPlaying(
                                    id,
                                    musicUri,
                                    musicName,
                                    artistName,
                                    albumArt,
                                    duration,
                                )

                            }

                        }

                        sendNotification(musicUri, musicName, artistName, albumArt, duration)

                    }
//                }
//                    }
                } catch (e: Exception) {
                    play()
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
        return START_NOT_STICKY

    }


    fun sendNotification(
        musicUri: String,
        musicName: String,
        artistName: String,
        albumArt: String,
        duration: Float
    ) {

        val mediaSession = MediaSessionCompat(this, "PlayerService")

        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
               play()
            }

            override fun onPause() {
               pause()
            }

            override fun onSkipToNext() {
               next()
                print("notificationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn")
            }

            override fun onSkipToPrevious() {
              previous()
            }

            override fun onSeekTo(pos: Long) {
                seekToPosition(pos)
            }

            override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
                return super.onMediaButtonEvent(mediaButtonEvent)
            }
        })

        mediaSession.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )

        val metadataBuilder = MediaMetadataCompat.Builder().apply {

            putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, musicName)
            putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, artistName)
            putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, albumArt)

            putString(MediaMetadata.METADATA_KEY_TITLE, musicName)
            putString(MediaMetadata.METADATA_KEY_ARTIST, artistName)
            putLong(MediaMetadata.METADATA_KEY_DURATION, duration.toLong())
            putBitmap(MediaMetadata.METADATA_KEY_ART, BitmapFactory.decodeFile(albumArt))

        }

        mediaSession.setMetadata(metadataBuilder.build())

        val stateActions = PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SEEK_TO

        mediaSession.setPlaybackState(PlaybackStateCompat.Builder()

            .setState(if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                player.currentPosition,
                1f)
            .setActions(stateActions)
//            .addCustomAction(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS.toString(),resources.getResourceName(R.drawable.ic_round_skip_backward),R.drawable.ic_round_skip_backward)
//            .addCustomAction(PlaybackStateCompat.ACTION_SKIP_TO_NEXT.toString(),resources.getResourceName(R.drawable.ic_round_skip_forward),R.drawable.ic_round_skip_forward)

            .build())

        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)
            .setMediaSession(mediaSession.sessionToken)

        val notification = NotificationCompat.Builder(this, CHANNEL_NAME)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(style)
            .setContentTitle(musicName)
            .setContentText(artistName)
            .addAction(R.drawable.ic_round_skip_backward, "prev", prevPendingIntent())
            .addAction(
                if (!player.isPlaying) R.drawable.ic_round_play else R.drawable.ic_round_pause,
                "play_pause",
                playPausePendingIntent()
            )
            .addAction(R.drawable.ic_round_skip_forward, "next", nextPendingIntent())
            .setSmallIcon(R.drawable.ic_music_selected)
            .setLargeIcon(BitmapFactory.decodeFile(albumArt))
            .build()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startForeground(Notification_ID, notification)
            }

        } else {

            startForeground(Notification_ID, notification)

        }


    }

    fun prevPendingIntent(): PendingIntent {

        val intent = Intent(this, PlayerService::class.java).apply {
            action = prev
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

    fun playPausePendingIntent(): PendingIntent {

        val intent = Intent(this, PlayerService::class.java).apply {
            action = playPause
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

    fun nextPendingIntent(): PendingIntent {

        val intent = Intent(this, PlayerService::class.java).apply {
            action = next
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

    }


    fun changePlayList(mediaItems: ArrayList<MediaItem>, allNextUpList: List<NextUpSongs>) {
        player.clearMediaItems()
        allNextUpList2 = allNextUpList


        player.addMediaItems(mediaItems)
    }

    fun play() {

        scope.launch {


//                updateIsPlaying(nowPlaying?.id, false)


            isPlaying =true
            player.playWhenReady = true
            player.prepare()
            updateIsPlaying(nowPlaying?.id, true)

            Log.d(TAG, "play: from service" + isPlaying)
        }
    }

    fun pause() {
        player.pause()
        isPlaying = false
    }

    private fun upDateDuration() {

        currentDuration = PlayerService.playerServiceInstance?.player?.currentPosition!!

//        if (isPlaying)

        Handler().postDelayed({
            upDateDuration()
        }, 1000)


    }

    fun seekToPosition(position: Long) {

        player.seekTo(position)
    }

    fun next() {

        if (player.hasNextMediaItem()) {
            player.seekToNext()
            player.play()
        } else {
            play()

        }
        isPlaying =true
    }

    fun previous() {

        if (player.hasPreviousMediaItem()) {
            player.seekToPrevious()
            player.play()
        } else {
            play()
        }
        isPlaying =true
    }

    fun repeat(id: Int, repeat: Int) {
        scope2.launch {

            var value = 0

            if (repeat == 1)
                value = 2
            else if (repeat == 2)
                value = 3
            else if (repeat == 3)
                value = 1


            repository.updateRepeat(id, value)
        }


    }

    fun shuffle(id: Int, shuffle: Boolean) {
        scope2.launch {

            repository.updateShuffle(id, shuffle)

        }
    }

    fun updateNowPlaying(
        id: Int,
        musicUri: String,
        musicName: String,
        artistName: String,
        albumArt: String,
        duration: Float,
    ) {
        scope2.launch {

            repository.updateNowPlaying(id, musicUri, musicName, artistName, albumArt, duration)
        }
    }

    private fun updateIsPlaying(id: Int?, value: Boolean) {
        scope2.launch {
            if (id != null) {
                repository.updateNowPlayingIsPlaying(id = id, value)
            }
        }
    }
}



























