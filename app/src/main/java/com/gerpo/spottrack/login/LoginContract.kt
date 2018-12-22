package com.gerpo.spottrack.login

import android.content.Intent
import android.view.View
import com.spotify.sdk.android.authentication.AuthenticationRequest

interface LoginContract {
    interface LoginView {
        fun onLoginClick(view: View)
        fun openSpotifyLogin(request: AuthenticationRequest)
        fun openMainActivity()
        fun showToast(message: String)
    }

    interface LoginPresenter {
        fun checkLoginStatus()
        fun login()
        fun handleLogin(resultCode: Int, intent: Intent)
    }
}
