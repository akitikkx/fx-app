package com.ahmedtikiwa.fxapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.ahmedtikiwa.fxapp.work.RefreshCurrenciesWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class FxApplication : Application(), Configuration.Provider {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()


    private fun delayedInit() {
        applicationScope.launch {
            launchBackgroundTasks()
        }
    }

    private fun launchBackgroundTasks() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val workManager = WorkManager.getInstance(this)

        val refreshCurrenciesRequest =
            PeriodicWorkRequestBuilder<RefreshCurrenciesWorker>(7, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        workManager.enqueueUniquePeriodicWork(
            RefreshCurrenciesWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            refreshCurrenciesRequest
        )
    }
}