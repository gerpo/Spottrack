package com.gerpo.spottrack.spotify

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import com.gerpo.spottrack.database.TrackDataSource
import com.gerpo.spottrack.database.entities.PlayedTrack
import com.gerpo.spottrack.database.entities.Track
import com.gerpo.spottrack.login.LoginActivity
import com.gerpo.spottrack.transformer.TrackTransformer
import kaaes.spotify.webapi.android.models.Pager
import retrofit.RetrofitError
import java.util.*
import javax.inject.Inject

class SpotifySync @Inject constructor(val context: Context, val prefs: SharedPreferences, val spotify: SpotifyService, val dao: TrackDataSource) {
    var isRunning = false

    init {
        prefs.edit().putLong("lastCheckTimestamp", System.currentTimeMillis() - (1000 * 60 * 60 *24 *7)).apply()
    }

    suspend fun checkRecentTracks() {
        if (isRunning || !blockPeriodOver()) return
        isRunning = true

        val trackedTracks = dao.getAllIncomplete() ?: return
        val recentlyPlayed = getPlayedTracks()

        val played = trackedTracks.filter { it.spotify_uri in recentlyPlayed.map { it.spotify_uri } }

        dao.markAsComplete(played.map { it.id })
        isRunning = false
    }

    private fun blockPeriodOver(): Boolean {
        val limit = 1000 * 30
        val lastCheck = prefs.getLong("lastCheckTimestamp", System.currentTimeMillis() - (1000 * 60 * 60 *24 *7))

        return System.currentTimeMillis() > (lastCheck + limit)
    }

    private fun getPlayedTracks(before: Long = System.currentTimeMillis()): List<Track> {
        val resultTracks = mutableListOf<Track>()
        val result: Pager<PlayedTrack>?

        val options = HashMap<String, Any>()
        options[SpotifyService.LIMIT] = 50
        options[SpotifyService.BEFORE] = before

        try {
            result = spotify.getRecentlyPlayed(options)
            resultTracks.addAll(TrackTransformer.fromPlayedTrack(result.items))
            if (result.next != null) {
                val curBefore = result.next.substringAfter("before=").substringBefore("&").toLong()
                if (curBefore >= prefs.getLong("lastCheckTimestamp", 0)) resultTracks.addAll(getPlayedTracks(curBefore))
            }
        } catch (e: RetrofitError) {
            ContextCompat.startActivity(context, Intent(context, LoginActivity::class.java), null)
        }


        prefs.edit().putLong("lastCheckTimestamp", System.currentTimeMillis()).apply()
        return resultTracks
    }
}