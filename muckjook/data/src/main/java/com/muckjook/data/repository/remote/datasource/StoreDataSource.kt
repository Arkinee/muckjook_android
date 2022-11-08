package com.muckjook.data.repository.remote.datasource

import com.muckjook.data.remote.model.StoreResponse
import com.muckjook.domain.utils.RemoteErrorEmitter

/**
 * StoreDataSourceImpl의 실질적 구현체
 * 이곳에서 API 호출, 다시 view로 보여줌
 */

interface StoreDataSource {
    suspend fun getStore(remoteErrorEmitter: RemoteErrorEmitter, owner: String) : List<StoreResponse>?
}