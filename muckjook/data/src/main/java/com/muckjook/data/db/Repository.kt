package com.muckjook.data.db

import androidx.lifecycle.LiveData

class Repository(mDatabase: AppDatabase) {

    private val dao = mDatabase.dao()
    val allShops: LiveData<List<ShopEntity>> = dao.getAll()
    companion object {
        private var sInstance: Repository? = null
        fun getInstance(database: AppDatabase): Repository {
            return sInstance
                ?: synchronized(this) {
                    val instance = Repository(database)
                    sInstance = instance
                    instance
                }
        }
    }

    suspend fun insert(entity: ShopEntity) {
        dao.insert(entity)
    }

    suspend fun delete(entity: ShopEntity) {
        dao.delete(entity)
    }

}