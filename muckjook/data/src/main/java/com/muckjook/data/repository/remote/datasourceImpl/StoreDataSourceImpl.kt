package com.muckjook.data.repository.remote.datasourceImpl

import com.muckjook.data.remote.api.StoreApi
import com.muckjook.data.remote.model.StoreResponse
import com.muckjook.data.repository.remote.datasource.StoreDataSource
import com.muckjook.data.utils.base.BaseRepository
import com.muckjook.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class StoreDataSourceImpl @Inject constructor(private val storeApi: StoreApi) : BaseRepository(), StoreDataSource{
    override suspend fun getStore(
        remoteErrorEmitter: RemoteErrorEmitter,
        owner: String
    ): List<StoreResponse>? {
        return safeApiCall(remoteErrorEmitter){storeApi.getStores(owner).body()}
    }
}