package com.example.relay.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)  //동일한 primary Key 가진 객체 있을 경우 덮어씌움(충돌방지)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM running_table ORDER BY timestamp")
    fun getAllRunSortedByDate(): LiveData<List<Run>>

//    @Query("SELECT SUM(timeInMillis) FROM running_table")
//    fun getTotalTimeInMillis(): LiveData<Long>
}