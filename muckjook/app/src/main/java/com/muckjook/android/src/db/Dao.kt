package com.muckjook.android.src.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {

    @Query("SELECT * from shop_table ORDER BY id ASC")
    fun getAll() : LiveData<List<ShopEntity>>

    //@Query("SELECT * from shop_table WHERE name=")
    //fun search(title:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: ShopEntity)

    @Query("DELETE FROM shop_table")
    fun deleteAll()

    @Update
    fun update(entity: ShopEntity)

    @Delete
    fun delete(entity: ShopEntity)

}