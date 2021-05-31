package com.ahmedtikiwa.fxapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahmedtikiwa.fxapp.database.DatabaseHistory
import com.ahmedtikiwa.fxapp.database.FXAppDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class HistoryPagingSource(private val dao: FXAppDao) : PagingSource<Int, DatabaseHistory>() {

    override fun getRefreshKey(state: PagingState<Int, DatabaseHistory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DatabaseHistory> {
        val currentPosition = params.key ?: HISTORY_START_INDEX

        return withContext(Dispatchers.IO) {
            val items = dao.getPagedHistory()

            try {
                LoadResult.Page(
                    data = items,
                    prevKey = if (currentPosition == HISTORY_START_INDEX) null else currentPosition - 1,
                    nextKey = if (items.isEmpty()) null else currentPosition + 1
                )
            } catch (e: IOException) {
                LoadResult.Error(e)
            }
        }
    }

    companion object {
        const val HISTORY_START_INDEX = 1
    }
}