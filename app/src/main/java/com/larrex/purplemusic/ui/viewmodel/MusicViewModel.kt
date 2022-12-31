package com.larrex.purplemusic.ui.viewmodel

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.larrex.purplemusic.MainActivity
import com.larrex.purplemusic.domain.model.AlbumItem
import com.larrex.purplemusic.domain.model.ArtistItemModel
import com.larrex.purplemusic.domain.model.SongItem
import com.larrex.purplemusic.domain.repository.Repository
import com.larrex.purplemusic.domain.room.NextUpSongs
import com.larrex.purplemusic.domain.room.NowPlaying
import com.larrex.purplemusic.domain.room.Playlist
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

private const val TAG = "MusicViewModel"

@HiltViewModel
class MusicViewModel @Inject constructor(
    private var repository: Repository,
    private var player: ExoPlayer,
    @ApplicationContext context: Context
) : ViewModel() {

    val searchSongsList = mutableStateListOf<SongItem>()
    var allSongsList: List<SongItem> = ArrayList<SongItem>()

    val searchNextUpList = mutableStateListOf<NextUpSongs>()
    var allNextUpList: List<NextUpSongs> = ArrayList<NextUpSongs>()

    val mediaItems: MutableList<MediaItem> =
        ArrayList<MediaItem>()

    var nowPlaying: NowPlaying? = null

    var isPlaying by mutableStateOf(false)
    var isPaused by mutableStateOf(false)

    var currentDuration by mutableStateOf(0L)
    var currentPositionSlider by mutableStateOf(0F)

    var playingFromName = ""
    var playingFromType = ""
    var repeat: Int = 1
    var shuffle = false

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    val scope = CoroutineScope(Dispatchers.IO)

    init {

        val mainActivityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0)
        var repeatType by mutableStateOf(0)


        mediaSession = MediaSessionCompat(context, "PlayerService")
        mediaSession.isActive = true
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player)
        val navigator = object : TimelineQueueNavigator(mediaSession) {

            override fun getMediaDescription(
                player: Player,
                windowIndex: Int
            ): MediaDescriptionCompat {

                return MediaDescriptionCompat.Builder().build()

            }
        }

        mediaSessionConnector.setQueueNavigator(navigator)

        Log.d(TAG, "init: " + player)
        CoroutineScope(Dispatchers.IO).launch {

            getAllSongs().collectLatest {
                allSongsList = it
            }

            getNextUps().collectLatest {
                allNextUpList = it
            }

        }

        CoroutineScope(Dispatchers.Main).launch {

            getNextUps().collectLatest {
                player.clearMediaItems()
                mediaItems.clear()
                it.forEach {

                    mediaItems.add(MediaItem.fromUri(it.songUri.toUri()))

                }.also {
//                    nowPlaying?.musicUri?.let { MediaItem.fromUri(it.toUri()) }
//                        ?.let { player.addMediaItem(it) }


                    player.addMediaItems(mediaItems)
                }

            }


        }

        scope.launch {
            getNowPlaying().collectLatest {
//
//                playingFromType = it.playingFromType
//                playingFromName = it.playingFromName
//                repeat = it.repeat
//                shuffle = it.shuffle
//                repeatType = it.repeat
                nowPlaying = it
//                Log.d(TAG, "pppppppppppppp: " + it.id)
            }
        }

        if (repeatType != 0) {

            player.repeatMode =
                if (repeatType == 1) Player.REPEAT_MODE_OFF else if (repeatType == 2) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_ONE


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
                        val duration = allNextUpList[player.currentMediaItemIndex].duration

                        Log.d(
                            TAG,
                            "onMediaItemTransition: 44 ${allNextUpList[player.currentMediaItemIndex].songName}"
                        )

                        if (isPlaying) {

                            scope.launch {

                                Log.d(TAG, "onMediaItemTransition id: $id")
                                if (id != null) {
                                    Log.d(TAG, "onMediaItemTransition id2: $id")


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

                        }
                    }
                }catch (e : Exception){

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

    }

    fun play(context: Context) {

        if (isPlaying) {


            player.pause()
            isPlaying = false

            if (isPaused) {
                player.play()
                isPaused = false
            }

            return
        }
        player.playWhenReady = true
        player.prepare()
        isPlaying = true
        currentDuration = player.currentPosition
        Log.d(TAG, "play: " + player)
        Log.d(TAG, "play: " + mediaItems.size)
        upDateDuration()
    }

    private fun upDateDuration() {

        currentDuration = player.currentPosition

        if (isPlaying)

            Handler().postDelayed({
                upDateDuration()
            }, 1000)


    }

    fun next() {


        if (player.hasNextMediaItem()) {
            player.seekToNext()
            player.play()
            isPlaying = true
            upDateDuration()
        }

    }

    fun seekToPosition(position: Long) {
        player.seekTo(position)
    }

    fun previous() {

        if (player.hasPreviousMediaItem()) {
            player.seekToPrevious()
            player.play()
            isPlaying = true
            upDateDuration()
        }


    }

    fun repeat(id: Int, repeat: Int) {
        scope.launch {

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
        scope.launch {

            repository.updateShuffle(id, shuffle)

        }
    }

    fun updateNowPlaying(
        id: Int,
        musicUri: String,
        musicName: String,
        artistName: String,
        albumArt: String,
        duration: Int,
    ) {
        repository.updateNowPlaying(id, musicUri, musicName, artistName, albumArt, duration)
        Log.d(TAG, "onMediaItemTransition: update $id")
    }

    fun updateNowPlayingWithTypeAndName(
        id: Int,
        musicUri: String,
        musicName: String,
        artistName: String,
        albumArt: String,
        duration: Int,
        playingFromType: String,
        playingFromName: String,
    ) {
        repository.updateNowPlayingWithTypeAndName(
            id,
            musicUri,
            musicName,
            artistName,
            albumArt,
            duration,
            playingFromType,
            playingFromName
        )
    }

    fun searchSongs(songName: String) {

        searchSongsList.clear()

        allSongsList.forEach {

            if (it.songName.toLowerCase().contains(songName.toLowerCase())) {
                searchSongsList.add(it)
            }

        }

    }

    fun searchNextUps(songName: String) {

        searchNextUpList.clear()

        allNextUpList.forEach {

            if (it.songName.toLowerCase().contains(songName.toLowerCase())) {
                searchNextUpList.add(it)
            }

        }

    }

    fun getAllSongs(): Flow<List<SongItem>> {

        return repository.getAllSongs()
    }

    fun getAllSongsFromAlbum(albumName: String): Flow<List<SongItem>> {

        return repository.getAllSongsFromAlbum(albumName)
    }

    fun getAllAlbums(): Flow<List<AlbumItem>> {

        return repository.getAllAlbums()
    }

    fun getAllAlbumsFromArtist(artistName: String): Flow<List<AlbumItem>> {

        return repository.getAllAlbumFromArtist(artistName)
    }

    fun getAllArtist(): Flow<List<ArtistItemModel>> {

        return repository.getAllArtist()
    }

    fun insertNowPlaying(nowPlaying: NowPlaying) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertNowPlaying(nowPlaying)
        }
    }

    fun deleteNowPlaying() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteNowPlay()
        }
    }

    fun getNowPlaying(): Flow<NowPlaying> {

        return repository.getNowPlaying()
    }

    fun insertNextUps(nextUps: List<NextUpSongs>) {

        repository.insertNextUps(nextUps)

    }

    fun getNextUps(): Flow<List<NextUpSongs>> {

        return repository.getNextUps()
    }

    fun deleteNextUps() {
        repository.deleteNextUps()
    }

    fun insertPlaylist(playlist: Playlist) {

        repository.insertPlaylist(playlist)

    }

    fun insertToPlaylist(playlist: List<Playlist>) {

        repository.insertToPlaylist(playlist)

    }

    fun getPlaylistItem(): Flow<List<Playlist>> {
        return repository.getPlaylistItem()
    }

    fun getPlaylistItemImages(playlistId: Long): Flow<List<Playlist>> {
        Log.d(TAG, "getPlaylistItemImages: $playlistId")
        return repository.getPlaylistItemImages(playlistId)

    }

    fun getPlaylistContentWithId(playlistId: Long): Flow<List<Playlist>> {
        return repository.getPlaylistContentWithId(playlistId)
    }

    fun deleteSingleItemFromAPlaylist(id: Int) {
        repository.deleteSingleItemFromAPlaylist(id)
    }

    fun deleteAPlaylist(ids: List<Int>) {
        repository.deleteAPlaylist(ids)
    }
}