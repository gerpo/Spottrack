package com.gerpo.spottrack.dagger.modules

import android.content.SharedPreferences
import com.gerpo.spottrack.overview.OverviewContract
import com.gerpo.spottrack.overview.OverviewFragment
import com.gerpo.spottrack.overview.OverviewPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
abstract class OverviewFragmentModule {
    @Binds
    abstract fun bindOverviewView(view: OverviewFragment): OverviewContract.OverviewView

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providePresenter(view: OverviewContract.OverviewView, prefs: SharedPreferences): OverviewContract.OverviewPresenter {
            return OverviewPresenter(view, prefs)
        }
    }
}