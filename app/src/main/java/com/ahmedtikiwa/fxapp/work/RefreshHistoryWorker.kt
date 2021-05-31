package com.ahmedtikiwa.fxapp.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmedtikiwa.fxapp.repository.FXAppRepository
import com.ahmedtikiwa.fxapp.util.DateUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope

@HiltWorker
class RefreshHistoryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val fxAppRepository: FXAppRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            refreshHistory(fxAppRepository)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    /**
     * Refreshes the cached historical data. Note that since the endpoint
     * only caters for one date at a time, the request will run through
     * all the different dates one at a time as the endpoint does not provide
     * data for a specified range but rather a specified date
     */
    private suspend fun refreshHistory(repository: FXAppRepository) {
        val previousThirtyDays = DateUtil.getPastThirtyDateExclWeekends()
        if (previousThirtyDays.isNotEmpty()) {
            repository.clearHistory()
            for (date in previousThirtyDays) {
                repository.getHistorical(date)
            }
        }
    }

    companion object {
        const val WORK_NAME = "RefreshHistoryWorker"
    }

}