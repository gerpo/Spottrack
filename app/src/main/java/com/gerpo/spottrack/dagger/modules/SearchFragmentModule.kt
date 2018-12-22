package com.gerpo.spottrack.dagger.modules

import android.content.Context
import com.gerpo.spottrack.search.SearchContract
import com.gerpo.spottrack.search.SearchFragment
import com.gerpo.spottrack.search.SearchPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
abstract class SearchFragmentModule {

    @Binds
    abstract fun bindSearchView(view: SearchFragment): SearchContract.SearchView

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providePresenter(view: SearchContract.SearchView, context: Context): SearchContract.SearchPresenter {
            return SearchPresenter(view, context)
        }
    }
}