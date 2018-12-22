package com.gerpo.spottrack.dagger.modules

import com.gerpo.spottrack.database.PlaylistDataSource
import com.gerpo.spottrack.database.TrackDataSource
import com.gerpo.spottrack.transformer.PlaylistTransformer
import dagger.Module
import dagger.Provides

@Module
class  TransformerModule {

    @Provides
    fun providesPlaylistTransformer(pDao: PlaylistDataSource, tDao: TrackDataSource): PlaylistTransformer
    {
        return PlaylistTransformer(pDao, tDao)
    }
}