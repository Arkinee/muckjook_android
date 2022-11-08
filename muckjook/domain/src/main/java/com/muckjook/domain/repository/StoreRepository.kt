package com.muckjook.domain.repository

import com.muckjook.domain.model.StoreResponse
import com.muckjook.domain.utils.RemoteErrorEmitter

interface StoreRepository {
    suspend fun getStore(remoteErrorEmitter: RemoteErrorEmitter, owner : String) : List<StoreResponse>?
}