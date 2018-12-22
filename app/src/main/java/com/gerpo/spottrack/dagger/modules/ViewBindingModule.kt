package com.gerpo.spottrack.dagger.modules

import com.gerpo.spottrack.detail.DetailActivity
import com.gerpo.spottrack.login.LoginActivity
import com.gerpo.spottrack.main.MainActivity
import com.gerpo.spottrack.overview.OverviewFragment
import com.gerpo.spottrack.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ViewBindingModule {
    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [DetailActivityModule::class])
    abstract fun contributeDetailActivity(): DetailActivity

    @ContributesAndroidInjector(modules = [OverviewFragmentModule::class])
    abstract fun contributeOverviewFragment(): OverviewFragment

    @ContributesAndroidInjector(modules = [SearchFragmentModule::class])
    abstract fun contributeSearchFragment(): SearchFragment

}