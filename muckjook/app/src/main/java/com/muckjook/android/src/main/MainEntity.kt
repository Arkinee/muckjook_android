package com.muckjook.android.src.main

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class MainEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int, var name: String){

}