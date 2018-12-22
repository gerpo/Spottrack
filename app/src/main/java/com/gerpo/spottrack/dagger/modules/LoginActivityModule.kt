package com.gerpo.spottrack.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import com.gerpo.spottrack.login.LoginActivity
import com.gerpo.spottrack.login.LoginContract
import com.gerpo.spottrack.login.LoginPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
abstract class LoginActivityModule {

    @Binds
    abstract fun bindView(view: LoginActivity): LoginContract.LoginView

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providePresenter(view: LoginContract.LoginView, prefs:SharedPreferences): LoginContract.LoginPresenter {

            return LoginPresenter(view, prefs)
        }
    }
}