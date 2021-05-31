package com.ahmedtikiwa.fxapp.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmedtikiwa.fxapp.repository.FXAppRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope

@HiltWorker
class RefreshCurrenciesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val fxAppRepository: FXAppRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            refreshCurrencies(fxAppRepository)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    /**
     * Refresh the saved list of currently supported currencies
     */
    private suspend fun refreshCurrencies(repository: FXAppRepository) {
        repository.refreshCurrencies()
    }

    companion object {
        const val WORK_NAME = "RefreshCurrenciesWorker"
    }
}