package com.gerpo.spottrack.dagger.modules

import android.content.Context
import com.gerpo.spottrack.main.MainActivity
import com.gerpo.spottrack.main.MainContract
import com.gerpo.spottrack.main.MainPresenter
import com.gerpo.spottrack.overview.OverviewFragment
import com.gerpo.spottrack.search.SearchFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton


@Module
abstract class MainActivityModule {

    @Binds
    abstract fun bindView(view: MainActivity): MainContract.MainView

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providePresenter(view: MainContract.MainView, context: Context): MainContract.MainPresenter {
            return MainPresenter(view, context)
        }

        @JvmStatic
        @Provides
        fun provideSearchFragment() : SearchFragment = SearchFragment()

        @JvmStatic
        @Provides
        fun provideOverviewFragment() : OverviewFragment = OverviewFragment()
    }
}