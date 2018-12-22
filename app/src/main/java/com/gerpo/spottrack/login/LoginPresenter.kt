package com.gerpo.spottrack.login

import android.content.Intent
import android.content.SharedPreferences
import com.gerpo.spottrack.BuildConfig
import com.gerpo.spottrack.login.LoginContract.LoginView
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import javax.inject.Inject

class LoginPresenter @Inject constructor(private val view: LoginView, val prefs: SharedPreferences) : LoginContract.LoginPresenter {


    override fun checkLoginStatus() {
        if (prefs.getBoolean("hasLogin", false) && prefs.getLong("lastLogin", 0) > System.currentTimeMillis() - LOGIN_TIMEOUT) {
            this.view.openMainActivity()
        } else if (prefs.getBoolean("hasLogin", false)) {
            makeLoginRequest()
        }
    }

    private fun makeLoginRequest(): AuthenticationRequest {

        val builder = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("user-read-private", "user-read-recently-played"))

        return builder.build()
    }

    override fun handleLogin(resultCode: Int, intent: Intent) {

        val response = AuthenticationClient.getResponse(resultCode, intent)

        when (response.type) {
            // Response was successful and contains auth token
            AuthenticationResponse.Type.TOKEN -> {
                // Handle successful response
                prefs.edit().putBoolean("hasLogin", true).apply()
                prefs.edit().putString("token", response.accessToken).apply()
                prefs.edit().putLong("lastLogin", System.currentTimeMillis() - 1000).apply()
                this.view.openMainActivity()
            }

            // Auth flow returned an error
            AuthenticationResponse.Type.ERROR ->
                // Handle error response
                this.view.showToast("There was a Error.")

            // Most likely auth flow was cancelled
            else -> this.view.showToast("Just log in later.")
        }
    }

    override fun login() {
        val request = makeLoginRequest()

        this.view.openSpotifyLogin(request)

    }

    companion object {
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        private const val CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID
        private const val REDIRECT_URI = "spottrack://login_handle"
        private const val LOGIN_TIMEOUT = 1000 * 60 * 60
    }
}
