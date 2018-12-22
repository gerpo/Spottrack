package com.gerpo.spottrack.main

import android.content.Context
import com.gerpo.spottrack.database.AppDatabase
import javax.inject.Inject

class MainPresenter @Inject constructor(view: MainContract.MainView, context: Context) : MainContract.MainPresenter
