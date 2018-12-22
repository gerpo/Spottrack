package com.gerpo.spottrack

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter : CoroutineScope {
    val compositeJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    fun onDestroy() {
        compositeJob.cancel() // Cancel job on activity destroy. After destroy all children jobs will be cancelled automatically
    }
}