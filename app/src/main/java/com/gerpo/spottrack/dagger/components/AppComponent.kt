package com.gerpo.spottrack.dagger.components

import com.gerpo.spottrack.App
import com.gerpo.spottrack.dagger.modules.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@SuppressWarnings("unchecked")
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    DatabaseModule::class,
    TransformerModule::class,
    SpotifyModule::class,
    ViewBindingModule::class])
interface AppComponent : AndroidInjector<App>