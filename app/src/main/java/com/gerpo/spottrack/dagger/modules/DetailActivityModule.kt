package com.gerpo.spottrack.dagger.modules

import com.gerpo.spottrack.database.PlaylistDataSource
import com.gerpo.spottrack.detail.DetailActivity
import com.gerpo.spottrack.detail.DetailContract
import com.gerpo.spottrack.detail.DetailPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DetailActivityModule {
    @Binds
    abstract fun bindView(view: DetailActivity): DetailContract.DetailView

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providePresenter(view: DetailContract.DetailView, playlistDataSource: PlaylistDataSource): DetailContract.DetailPresenter {
            return DetailPresenter(view, playlistDataSource)
        }
    }
}