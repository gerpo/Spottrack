package com.gerpo.spottrack.spotify

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.gerpo.spottrack.login.LoginActivity
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

abstract class SpotifyCallback<T>(var context: Context) : Callback<T> {
    abstract override fun success(resultData: T, response: Response)

    override fun failure(error: RetrofitError) {
        Log.d("Spotify request failed.", error.toString())

        if (error.response.status == 401) {
            this.context.getSharedPreferences("com.gerpo.spottrack", MODE_PRIVATE).apply {
                edit().putBoolean("hasLogin", false).apply()
                edit().remove("token").apply()
            }
            startActivity(this.context, Intent(this.context, LoginActivity::class.java), null)
        }
    }
}