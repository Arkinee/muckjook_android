package com.muckjook.data.repository

import com.muckjook.data.mapper.Mapper
import com.muckjook.data.repository.remote.datasource.StoreDataSource
import com.muckjook.domain.model.StoreResponse
import com.muckjook.domain.repository.StoreRepository
import com.muckjook.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(private val storeDataSource: StoreDataSource) : StoreRepository{
    override suspend fun getStore(
        remoteErrorEmitter: RemoteErrorEmitter,
        owner: String
    ): List<StoreResponse>? {
        return Mapper.mapperStore(storeDataSource.getStore(remoteErrorEmitter, owner))
    }
}