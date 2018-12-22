package com.gerpo.spottrack.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.gerpo.spottrack.R
import com.gerpo.spottrack.main.MainActivity
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity(), LoginContract.LoginView {

    @Inject
    lateinit var presenter: LoginContract.LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()
        this.presenter.checkLoginStatus()
    }

    override fun onLoginClick(view: View) {
        this.presenter.login()
    }

    override fun openSpotifyLogin(request: AuthenticationRequest) {
        AuthenticationClient.openLoginActivity(this, LOGIN_REQUEST_CODE, request)
    }

    override fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode != LOGIN_REQUEST_CODE) return

        this.presenter.handleLogin(resultCode, intent!!)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        private const val LOGIN_REQUEST_CODE = 1337
    }


}
