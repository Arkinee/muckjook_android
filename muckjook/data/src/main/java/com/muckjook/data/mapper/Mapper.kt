package com.muckjook.data.mapper

import com.muckjook.data.remote.model.StoreResponse

/**
 * Domain은 data layer를 모르므로 data에서 domain layer의 data class로 자료형을 변환해서 보내줌
 */
object Mapper {
    fun mapperStore(response: List<StoreResponse>?): List<com.muckjook.domain.model.StoreResponse>?{
        return if (response != null){
            response.toDomain()
        }else null
    }

    fun List<StoreResponse>.toDomain(): List<com.muckjook.domain.model.StoreResponse>{
        return this.map {
            com.muckjook.domain.model.StoreResponse(
                it.name,
                it.id,
                it.date,
                it.latitude,
                it.longitude,
                it.imgUrls
            )
        }
    }
}