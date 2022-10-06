package com.larrex.purplemusic.domain.exoplayer.service

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.media.MediaBrowserServiceCompat

class PlayerService : MediaBrowserServiceCompat() {
    /**
     * Called to get the root information for browsing by a particular client.
     *
     *
     * The implementation should verify that the client package has permission
     * to access browse media information before returning the root id; it
     * should return null if the client is not allowed to access this
     * information.
     *
     *
     * @param clientPackageName The package name of the application which is
     * requesting access to browse media.
     * @param clientUid The uid of the application which is requesting access to
     * browse media.
     * @param rootHints An optional bundle of service-specific arguments to send
     * to the media browse service when connecting and retrieving the
     * root id for browsing, or null if none. The contents of this
     * bundle may affect the information returned when browsing.
     * @return The [BrowserRoot] for accessing this app's content or null.
     * @see BrowserRoot.EXTRA_RECENT
     *
     * @see BrowserRoot.EXTRA_OFFLINE
     *
     * @see BrowserRoot.EXTRA_SUGGESTED
     */
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    /**
     * Called to get information about the children of a media item.
     *
     *
     * Implementations must call [result.sendResult][Result.sendResult]
     * with the list of children. If loading the children will be an expensive
     * operation that should be performed on another thread,
     * [result.detach][Result.detach] may be called before returning from
     * this function, and then [result.sendResult][Result.sendResult]
     * called when the loading is complete.
     *
     *
     * In case the media item does not have any children, call [Result.sendResult]
     * with an empty list. When the given `parentId` is invalid, implementations must
     * call [result.sendResult][Result.sendResult] with `null`, which will invoke
     * [MediaBrowserCompat.SubscriptionCallback.onError].
     *
     *
     * @param parentId The id of the parent media item whose children are to be
     * queried.
     * @param result The Result to send the list of children to.
     */
    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }
}