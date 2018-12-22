package com.gerpo.spottrack

import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : DaggerFragment(), CoroutineScope {
    var compositeJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    override fun onDestroy() {
        super.onDestroy()
        compositeJob.cancel() // Cancel job on activity destroy. After destroy all children jobs will be cancelled automatically
    }
}