package com.example.relay.repository

import com.example.relay.db.Run
import com.example.relay.db.RunDao
import javax.inject.Inject

class RunningRepository @Inject constructor(
    val runDao: RunDao
){
    suspend fun insertRun(run: Run) = runDao.insertRun(run)
    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)
    fun getAllRunSortedByDate() = runDao.getAllRunSortedByDate()
//    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()

}