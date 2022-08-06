package com.muckjook.android.src.main.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.muckjook.android.src.main.MainEntity

@Dao
interface MainDaoInterface {

    // 데이터 베이스 불러오기
    @Query("SELECT * from user_table ORDER BY id ASC")
    fun getAll(): LiveData<List<MainEntity>>

    // 데이터 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: MainEntity)

    // 데이터 전체 삭제
    @Query("DELETE FROM user_table")
    fun deleteAll()

    // 데이터 업데이트
    @Update
    fun update(entity: MainEntity);

    // 데이터 삭제
    @Delete
    fun delete(entity: MainEntity);


}