package com.gerpo.spottrack.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import com.gerpo.spottrack.database.TrackDataSource
import com.gerpo.spottrack.spotify.SpotifyApi
import com.gerpo.spottrack.spotify.SpotifyService
import com.gerpo.spottrack.spotify.SpotifySync
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SpotifyModule {
    @Provides
    @Singleton
    fun provideSpotifyApi(): SpotifyApi {
        return SpotifyApi()
    }

    @Provides
    fun provideSpotifyService(spotifyApi: SpotifyApi, sharedPrefs: SharedPreferences): SpotifyService {
        spotifyApi.setAccessToken(sharedPrefs.getString("token", ""))
        return spotifyApi.service
    }

    @Provides
    @Singleton
    fun provideSpotifySyncService(context: Context, prefs: SharedPreferences, spotify: SpotifyService, dao: TrackDataSource): SpotifySync {
        return SpotifySync(context, prefs, spotify, dao)
    }
}
