package com.muckjook.android.src.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muckjook.android.src.db.AppDatabase
import com.muckjook.android.src.db.Repository
import com.muckjook.android.src.db.ShopEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var name = String
    var category = String
    var latitude = String
    var longitude = String

    val Repository: Repository = Repository((AppDatabase.getDatabase(application, viewModelScope)))
    var allShops: LiveData<List<ShopEntity>> = Repository.allShops

    fun insert(entity: ShopEntity) = viewModelScope.launch(Dispatchers.IO) {
        Repository.insert(entity)
    }

    fun deleteAll(entity: ShopEntity) = viewModelScope.launch(Dispatchers.IO) {
        Repository.delete(entity)
    }

    fun getAll(): LiveData<List<ShopEntity>> {
        return allShops
    }

}