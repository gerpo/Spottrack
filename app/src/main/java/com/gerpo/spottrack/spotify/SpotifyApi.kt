package com.gerpo.spottrack.spotify

import retrofit.RequestInterceptor
import retrofit.RestAdapter
import retrofit.android.MainThreadExecutor
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SpotifyApi {
    val service: SpotifyService
    private var mAccessToken: String? = null

    constructor(httpExecutor: Executor, callbackExecutor: Executor) {
        this.service = this.init(httpExecutor, callbackExecutor)
    }

    private fun init(httpExecutor: Executor, callbackExecutor: Executor): SpotifyService {
        val restAdapter = RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC).setExecutors(httpExecutor, callbackExecutor).setEndpoint(SPOTIFY_WEB_API_ENDPOINT).setRequestInterceptor(WebApiAuthenticator()).build()
        return restAdapter.create(SpotifyService::class.java) as SpotifyService
    }

    constructor() {
        val httpExecutor = Executors.newSingleThreadExecutor()
        val callbackExecutor = MainThreadExecutor()
        this.service = this.init(httpExecutor, callbackExecutor)
    }

    fun setAccessToken(accessToken: String?): SpotifyApi {
        this.mAccessToken = accessToken
        return this
    }

    private inner class WebApiAuthenticator : RequestInterceptor {

        override fun intercept(request: RequestInterceptor.RequestFacade) {
            if (this@SpotifyApi.mAccessToken != null) {
                request.addHeader("Authorization", "Bearer " + this@SpotifyApi.mAccessToken!!)
            }

        }
    }

    companion object {
        val SPOTIFY_WEB_API_ENDPOINT = "https://api.spotify.com/v1"
    }
}
