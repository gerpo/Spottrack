package com.gerpo.spottrack.dagger.modules

import android.content.Context
import com.gerpo.spottrack.spotify.SpotifyService
import com.gerpo.spottrack.database.AppDatabase
import com.gerpo.spottrack.database.dao.PlaylistDao
import com.gerpo.spottrack.database.PlaylistDataSource
import com.gerpo.spottrack.database.TrackDataSource
import com.gerpo.spottrack.database.dao.TrackDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providePlaylistDao(database: AppDatabase): PlaylistDao {
        return database.playlistDao()
    }

    @Provides
    @Singleton
    fun providePlaylistDataSource(dao: PlaylistDao, spotify: SpotifyService): PlaylistDataSource {
        return PlaylistDataSource(dao, spotify)
    }

    @Provides
    @Singleton
    fun provideTrackDao(database: AppDatabase): TrackDao {
        return database.trackDao()
    }

    @Provides
    @Singleton
    fun provideTrackDataSource(dao: TrackDao): TrackDataSource {
        return TrackDataSource(dao)
    }
}