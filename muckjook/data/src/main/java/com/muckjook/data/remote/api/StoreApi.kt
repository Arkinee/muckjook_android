package com.muckjook.data.remote.api

import com.muckjook.data.remote.model.StoreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit을 사용할 때 필요한 인터페이스.
 */
interface StoreApi {
    @GET("stores/{idx}")
    suspend fun getStores(@Path("idx") idx: String) : Response<List<StoreResponse>>
}