package com.muckjook.android.src.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_table")
data class ShopEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int, var name: String, var type: String, val latitude : Long, val longitude : Long){

}