package com.muckjook.domain.usecase

import com.muckjook.domain.repository.StoreRepository
import com.muckjook.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class GetStoreUseCase @Inject constructor(private val storeRepository: StoreRepository){
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, owner:String) = storeRepository.getStore(remoteErrorEmitter, owner)
}